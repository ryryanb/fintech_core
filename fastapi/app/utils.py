from passlib.context import CryptContext
import jwt
from datetime import datetime, timedelta
import os
from dotenv import load_dotenv
import base64
import logging

load_dotenv()

# Configure logging
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
ACCESS_TOKEN_EXPIRE_MINUTES = 30

# Load RSA private key (for signing)
try:
    private_key_b64 = os.getenv("PRIVATE_KEY")
    if not private_key_b64:
        raise ValueError("PRIVATE_KEY environment variable is not set")
    
    PRIVATE_KEY = base64.b64decode(private_key_b64).decode("utf-8")
    logger.debug("Successfully loaded private key")
except Exception as e:
    logger.error(f"Failed to load private key: {str(e)}")
    raise

def hash_password(password: str):
    return pwd_context.hash(password)

def verify_password(plain_password: str, hashed_password: str):
    return pwd_context.verify(plain_password, hashed_password)

def create_access_token(data: dict):
    try:
        to_encode = data.copy()
        expire = datetime.utcnow() + timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
        to_encode.update({"exp": expire})
        logger.debug("Creating token with algorithm RS256")
        token = jwt.encode(to_encode, PRIVATE_KEY, algorithm="RS256")
        logger.debug("Token created successfully")
        return token
    except Exception as e:
        logger.error(f"Failed to create access token: {str(e)}")
        raise
