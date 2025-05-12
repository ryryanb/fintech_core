from passlib.context import CryptContext
import jwt
from datetime import datetime, timedelta
import os
from dotenv import load_dotenv
import base64

load_dotenv()

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
ACCESS_TOKEN_EXPIRE_MINUTES = 30

# Load RSA private key (for signing)
#with open(os.getenv("JWT_PRIVATE_KEY_PATH"), "rb") as key_file:
    #PRIVATE_KEY = key_file.read()

private_key_b64 = os.getenv("PRIVATE_KEY")
PRIVATE_KEY = base64.b64decode(private_key_b64).decode("utf-8")

def hash_password(password: str):
    return pwd_context.hash(password)

def verify_password(plain_password: str, hashed_password: str):
    return pwd_context.verify(plain_password, hashed_password)

def create_access_token(data: dict):
    to_encode = data.copy()
    expire = datetime.utcnow() + timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    to_encode.update({"exp": expire})
    return jwt.encode(to_encode, PRIVATE_KEY, algorithm="RS256")
