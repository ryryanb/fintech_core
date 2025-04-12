from fastapi import FastAPI
from app.auth import router as auth_router
from app.database import Base, engine

app = FastAPI()

# Include your authentication routes
app.include_router(auth_router)

@app.on_event("startup")
async def startup():
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)
