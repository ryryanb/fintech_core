from fastapi import Depends, HTTPException
from app.database import AsyncSessionLocal
from fastapi.security import OAuth2PasswordBearer
import jwt
import os
from jwt import PyJWTError
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from contextlib import asynccontextmanager

# Your User model (SQLAlchemy ORM model)
from app.models import UserDB

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/login")
JWT_SECRET = os.getenv("JWT_SECRET")

@asynccontextmanager
async def get_session():
    async with AsyncSessionLocal() as session:
        try:
            yield session
            await session.commit()
        except Exception as e:
            await session.rollback()
            raise e

async def get_db():
    async with get_session() as session:
        yield session

async def get_current_user(
        token: str = Depends(oauth2_scheme),
        session: AsyncSession = Depends(get_db)
):
    try:
        payload = jwt.decode(token, JWT_SECRET, algorithms=["HS256"])
        print(payload)
        email = payload.get("email")

        if not email:
            raise HTTPException(status_code=401, detail="Invalid token")

        async with session.begin():
            result = await session.execute(select(UserDB).where(UserDB.email == email))
            user = result.scalar_one_or_none()

            if not user:
                raise HTTPException(status_code=404, detail="User not found")

            return user

    except PyJWTError:
        raise HTTPException(status_code=401, detail="Invalid token")

async def get_or_create_user_from_google(session: AsyncSession, google_user_info: dict):
    try:
        async with session.begin():
            email = google_user_info["email"]
            name = google_user_info.get("name", "")
            profile_picture = google_user_info.get("picture", "")

            # Check if the user already exists
            result = await session.execute(select(UserDB).where(UserDB.email == email))
            user = result.scalar_one_or_none()

            if user:
                return user

            # Create new user
            new_user = UserDB(
                email=email,
                name=name,
                profile_picture=profile_picture,
                is_active=True,
                is_google_account=True
            )

            session.add(new_user)
            await session.flush()
            await session.refresh(new_user)
            return new_user

    except Exception as e:
        await session.rollback()
        raise HTTPException(status_code=500, detail=f"Database error: {str(e)}")

