from fastapi import FastAPI
from app.auth import router as auth_router
from app.database import Base, engine
from app.auth_router import router as oauth_router

app = FastAPI()

# Include your authentication routes
app.include_router(auth_router)
app.include_router(oauth_router)

@app.on_event("startup")
async def startup():
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)
