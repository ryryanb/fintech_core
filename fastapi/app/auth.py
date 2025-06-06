import logging
from fastapi import APIRouter, Depends, HTTPException
from app.schemas import User, LoginRequest
#from app.crud import get_current_user, create_user
from app.utils import verify_password, create_access_token, hash_password
from app.deps import get_db, get_current_user
from sqlalchemy.ext.asyncio import AsyncSession
from app.models import UserDB
from sqlalchemy.future import select

router = APIRouter()
logger = logging.getLogger("auth_logger")
logger.setLevel(logging.DEBUG)

@router.post("/register")
async def register(user: User, db: AsyncSession = Depends(get_db)):

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

@router.post("/login")
async def login(request: LoginRequest, db: AsyncSession = Depends(get_db)):
    logger.debug(f"Received login request: email={request.email}, tenant_id={request.tenant_id}")

    try:
        # Log the start of database query
        logger.debug("Querying database for user...")
        
        db_user = await db.execute(
            select(UserDB).filter(
                UserDB.email == request.email,
                UserDB.tenant_id == request.tenant_id
            )
        )
        db_user = db_user.scalar_one_or_none()
        
        # Log user lookup result
        if db_user:
            logger.debug(f"User found in database: {db_user.email}")
        else:
            logger.debug(f"No user found with email={request.email} and tenant_id={request.tenant_id}")
            raise HTTPException(status_code=401, detail="Invalid credentials")

        # Log password verification attempt
        logger.debug("Verifying password...")
        if not verify_password(request.password, db_user.password):
            logger.debug("Password verification failed")
            raise HTTPException(status_code=401, detail="Invalid credentials")
        
        logger.debug("Password verified successfully")

        # Log token creation attempt
        logger.debug("Attempting to create access token with RSA-256...")
        try:
            token = create_access_token({
                "email": request.email,
                "tenant_id": request.tenant_id,
                "roles": ["user"]
            })
            logger.debug("Token generated successfully with RSA-256")
        except Exception as token_error:
            logger.exception(f"Token generation failed: {str(token_error)}")
            raise HTTPException(status_code=500, detail="Error generating authentication token")

        return {"access_token": token, "token_type": "bearer"}
        
    except HTTPException as he:
        # Re-raise HTTP exceptions as they are already properly formatted
        raise he
    except Exception as e:
        # Log the full error details
        logger.exception(f"Unexpected error during login: {str(e)}")
        logger.error(f"Error type: {type(e).__name__}")
        logger.error(f"Error details: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Internal Server Error: {str(e)}")


@router.get("/protected")
async def protected(current_user: dict = Depends(get_current_user)):
    return {"user": current_user}
