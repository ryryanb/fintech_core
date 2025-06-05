from fastapi import FastAPI
from app.auth import router as auth_router
from app.database import Base, engine
from app.auth_router import router as oauth_router
from fastapi.middleware.cors import CORSMiddleware
import os

app = FastAPI()

# Allow requests from the frontend
origins = [
    "http://localhost:3000",  # React dev server
    "http://127.0.0.1:3000",
    "https://fintech-core-frontend.vercel.app",  # Vercel deployed frontend
    "https://transaction-frontend-three.vercel.app",
    "https://*.vercel.app"
]

# Configure CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["GET", "POST", "PUT", "DELETE", "OPTIONS"],  # Explicitly list allowed methods
    allow_headers=["*"],
    expose_headers=["*"],
    max_age=600,  # Cache preflight requests for 10 minutes
)

# Include your authentication routes
app.include_router(auth_router)
app.include_router(oauth_router)

@app.get("/health")
async def health_check():
    return {"status": "healthy"}

@app.on_event("startup")
async def startup():
    if not os.environ.get("VERCEL"):
        async with engine.begin() as conn:
            await conn.run_sync(Base.metadata.create_all)
