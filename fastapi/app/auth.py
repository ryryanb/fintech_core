from fastapi import APIRouter, Depends, HTTPException
from app.schemas import User, LoginRequest
#from app.crud import get_current_user, create_user
from app.utils import verify_password, create_access_token, hash_password
from app.deps import get_db, get_current_user
from sqlalchemy.ext.asyncio import AsyncSession
from app.models import UserDB
from sqlalchemy.future import select

router = APIRouter()

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
    db_user = await db.execute(
        select(UserDB).filter(
            UserDB.email == request.email,
            UserDB.tenant_id == request.tenant_id
        )
    )
    db_user = db_user.scalar_one_or_none()
    if not db_user or not verify_password(request.password, db_user.password):
        raise HTTPException(status_code=401, detail="Invalid credentials")

    token = create_access_token({
        "email": request.email,
        "tenant_id": request.tenant_id,
        "roles": ["user"]
    })

    return {"access_token": token, "token_type": "bearer"}

@router.get("/protected")
async def protected(current_user: dict = Depends(get_current_user)):
    return {"user": current_user}
