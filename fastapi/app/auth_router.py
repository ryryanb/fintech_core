from fastapi import APIRouter, Request, HTTPException, Depends
from jose import jwt
import httpx
from app.config import settings
from app.deps import get_or_create_user_from_google, get_db
from sqlalchemy.ext.asyncio import AsyncSession

router = APIRouter()

GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token"
GOOGLE_USERINFO_URL = "https://openidconnect.googleapis.com/v1/userinfo"

@router.get("/auth/google/login")
async def google_login():
    # Redirect user to Google's OAuth2 consent page
    return {
        "auth_url": f"https://accounts.google.com/o/oauth2/v2/auth"
                    f"?client_id={settings.GOOGLE_CLIENT_ID}"
                    f"&response_type=code"
                    f"&scope=openid%20email%20profile"
                    f"&redirect_uri={settings.GOOGLE_REDIRECT_URI}"
    }

@router.get("/auth/google/callback")
async def google_callback(code: str, session: AsyncSession = Depends(get_db)):
    async with httpx.AsyncClient() as client:
        # Exchange authorization code for access token
        token_resp = await client.post(GOOGLE_TOKEN_URL, data={
            'client_id': settings.GOOGLE_CLIENT_ID,
            'client_secret': settings.GOOGLE_CLIENT_SECRET,
            'code': code,
            'grant_type': 'authorization_code',
            'redirect_uri': settings.GOOGLE_REDIRECT_URI,
        })
        token_resp.raise_for_status()
        tokens = token_resp.json()  # 👈 await here

        access_token = tokens.get('access_token')
        if not access_token:
            raise HTTPException(status_code=400, detail="Failed to obtain access token")

        # Use access token to fetch user info
        userinfo_resp = await client.get(GOOGLE_USERINFO_URL, headers={
            'Authorization': f'Bearer {access_token}'
        })
        userinfo_resp.raise_for_status()
        user_info = userinfo_resp.json()  # 👈 await here

        email = user_info.get('email')
        if not email:
            raise HTTPException(status_code=400, detail="Failed to get user email")

        # Now get or create the user in your database
        user = await get_or_create_user_from_google(session, user_info)  # ✅ pass the user_info
        print(user)
        print("here")
        # Now create your own JWT
        my_jwt = jwt.encode({
            "sub": str(user.id),  # or email, depends on your system
            "provider": "google",
            "email":user.email
        }, settings.JWT_SECRET, algorithm="HS256")

        return {"access_token": my_jwt, "token_type": "bearer"}
