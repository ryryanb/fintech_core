from sqlalchemy import Column, Integer, String, Boolean
from app.database import Base

class UserDB(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, autoincrement=True)
    email = Column(String, unique=True, index=True)
    password = Column(String, nullable=True)
    name = Column(String)
    address = Column(String)
    tenant_id = Column(Integer)
    profile_picture = Column(String)
    is_active = Column(Boolean, default=True)
    is_google_account = Column(Boolean, default=False)
