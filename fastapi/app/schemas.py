from pydantic import BaseModel

class User(BaseModel):
    email: str
    password: str
    name: str
    address: str
    tenant_id: int

    class Config:
        orm_mode = True

class LoginRequest(BaseModel):
    email: str
    password: str
    tenant_id: int
