from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    GOOGLE_CLIENT_ID: str
    GOOGLE_CLIENT_SECRET: str
    GOOGLE_REDIRECT_URI: str
    JWT_SECRET: str
    DEBUG: bool = False

    class Config:
        env_file = ".env"  # <- load from .env automatically
        extra = "ignore"

settings = Settings()
