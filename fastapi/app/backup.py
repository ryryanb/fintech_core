from fastapi import FastAPI, HTTPException, Depends
from fastapi.security import OAuth2PasswordBearer
from pydantic import BaseModel
from sqlalchemy import Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.ext.asyncio import AsyncSession, create_async_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.future import select  # Note: consider updating this to just `from sqlalchemy import select` if using SQLAlchemy 2.x
from passlib.context import CryptContext
from dotenv import load_dotenv
from jwt import PyJWTError  # Only this needed from jwt
import os
from datetime import datetime, timedelta

# Load environment variables from .env file
load_dotenv()
# Retrieve environment variables
DATABASE_URL = os.getenv("DATABASE_URL")
JWT_SECRET = os.getenv("JWT_SECRET")
ACCESS_TOKEN_EXPIRE_MINUTES = os.getenv("ACCESS_TOKEN_EXPIRE_MINUTES")

if not JWT_SECRET:
    raise RuntimeError("JWT_SECRET is not set. Please set it in your .env file.")

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
    name = Column(String)
    address = Column(String)
    tenant_id = Column(Integer)

class User(BaseModel):
    email: str
    password: str
    name: str
    address: str
    tenant_id: int

    class Config:
        orm_mode = True

class LoginRequest(BaseModel):
    email: str
    password: str
    tenant_id: int
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
    existing_user = await db.execute(
        select(UserDB).filter(
            UserDB.email == user.email,
            UserDB.tenant_id == user.tenant_id  # match tenant_id too
        )
    )
    existing_user = existing_user.scalar_one_or_none()

    if existing_user:
        raise HTTPException(status_code=400, detail="User already registered for this tenant")

    # Create a new user with hashed password
    db_user = UserDB(
        email=user.email,
        password=hash_password(user.password),
        tenant_id=user.tenant_id,
        name=user.name,
        address=user.address
    )
    db.add(db_user)
    await db.commit()

    return {"msg": "User registered successfully"}





@app.post("/login")
async def login(request: LoginRequest, db: AsyncSession = Depends(get_db)):
    # Check if user exists in the database
    db_user = await db.execute(
        select(UserDB).filter(
            UserDB.email == request.email,
            UserDB.tenant_id == request.tenant_id
        )
    )
    db_user = db_user.scalar_one_or_none()

    if not db_user or not verify_password(request.password, db_user.password):
        raise HTTPException(status_code=401, detail="Invalid credentials")

    # Set token expiration
    expire = datetime.utcnow() + timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)

    # Generate JWT token
    payload = {
        "email": request.email,
        "tenant_id": request.tenant_id,
        "roles": ["user"],  # default role
        "exp": expire
    }

    token = jwt.encode(payload, JWT_SECRET, algorithm="HS256")

    return {"access_token": token, "token_type": "bearer"}

# Initialize database (only needed for first run)
@app.on_event("startup")
async def startup():
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/login")

async def get_current_user(token: str = Depends(oauth2_scheme)):
    try:
        payload = jwt.decode(token, JWT_SECRET, algorithms=["HS256"])
        email: str = payload.get("email")
        if email is None:
            raise HTTPException(status_code=401, detail="Invalid token")
        return payload
    except PyJWTError:
        raise HTTPException(status_code=401, detail="Invalid token")
@app.get("/protected")
async def protected_route(current_user: dict = Depends(get_current_user)):
    return {"msg": f"Hello {current_user['email']}! You are authenticated.", "roles": current_user.get("roles")}

