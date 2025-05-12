from fastapi import FastAPI
from app.auth import router as auth_router
from app.database import Base, engine
from app.auth_router import router as oauth_router
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()

# Allow requests from the frontend
origins = [
    "http://localhost:3000",  # React dev server
    "http://127.0.0.1:3000",
    "https://transaction-frontend-three.vercel.app",
]
app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,           # Allowed origins
    allow_credentials=True,
    allow_methods=["*"],             # Allow all HTTP methods
    allow_headers=["*"],             # Allow all headers
)
# Include your authentication routes
app.include_router(auth_router)
app.include_router(oauth_router)

@app.on_event("startup")
async def startup():
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)
