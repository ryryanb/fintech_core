from pydantic import BaseModel, ConfigDict

class User(BaseModel):
    email: str
    password: str
    name: str
    address: str
    tenant_id: int

    model_config = ConfigDict(from_attributes=True)

class LoginRequest(BaseModel):
    email: str
    password: str
    tenant_id: int
