from fastapi import Depends, HTTPException
from sqlalchemy.ext.asyncio import AsyncSession
from app.database import SessionLocal
from fastapi.security import OAuth2PasswordBearer
import jwt
import os
from jwt import PyJWTError

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/login")
JWT_SECRET = os.getenv("JWT_SECRET")

async def get_db():
    async with SessionLocal() as session:
        yield session

async def get_current_user(token: str = Depends(oauth2_scheme)):
    try:
        payload = jwt.decode(token, JWT_SECRET, algorithms=["HS256"])
        email: str = payload.get("email")
        if email is None:
            raise HTTPException(status_code=401, detail="Invalid token")
        return payload
    except PyJWTError:
        raise HTTPException(status_code=401, detail="Invalid token")
