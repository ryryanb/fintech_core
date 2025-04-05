from fastapi import FastAPI, HTTPException, Depends
from pydantic import BaseModel
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from sqlalchemy.future import select
import jwt
from sqlalchemy.ext.asyncio import AsyncSession, create_async_engine
from dotenv import load_dotenv
import os
from passlib.context import CryptContext

# Load environment variables from .env file
load_dotenv()

# Retrieve environment variables
DATABASE_URL = os.getenv("DATABASE_URL")
JWT_SECRET = os.getenv("JWT_SECRET", "defaultsecret")

# Setup FastAPI app
app = FastAPI()

# Initialize password hashing context
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

# Define SQLAlchemy base class and engine
Base = declarative_base()
engine = create_async_engine(DATABASE_URL, echo=True)

# Create a sessionmaker for asynchronous sessions
SessionLocal = sessionmaker(
    bind=engine,
    class_=AsyncSession,
    expire_on_commit=False,
)

# Define the User model
class UserDB(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, autoincrement=True)
    email = Column(String, unique=True, index=True)
    password = Column(String)

# Pydantic model for user input
class User(BaseModel):
    email: str
    password: str

    class Config:
        orm_mode = True

# Dependency to get the database session
async def get_db():
    async with SessionLocal() as session:
        yield session

# Hash password function
def hash_password(password: str):
    return pwd_context.hash(password)

# Verify password function
def verify_password(plain_password: str, hashed_password: str):
    return pwd_context.verify(plain_password, hashed_password)

# Register user endpoint
@app.post("/register")
async def register(user: User, db: AsyncSession = Depends(get_db)):
    # Check if the user already exists
    existing_user = await db.execute(select(UserDB).filter(UserDB.email == user.email))
    existing_user = existing_user.scalar_one_or_none()

    if existing_user:
        raise HTTPException(status_code=400, detail="User already registered")

    # Create a new user with hashed password
    db_user = UserDB(email=user.email, password=hash_password(user.password))
    db.add(db_user)
    await db.commit()

    return {"msg": "User registered successfully"}

# Login user endpoint
@app.post("/login")
async def login(user: User, db: AsyncSession = Depends(get_db)):
    # Check if user exists in the database
    db_user = await db.execute(select(UserDB).filter(UserDB.email == user.email))
    db_user = db_user.scalar_one_or_none()

    if not db_user or not verify_password(user.password, db_user.password):
        raise HTTPException(status_code=401, detail="Invalid credentials")

    # Generate JWT token
    token = jwt.encode({"email": user.email}, JWT_SECRET, algorithm="HS256")
    return {"access_token": token}

# Initialize database (only needed for first run)
@app.on_event("startup")
async def startup():
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)
