import React, { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import axios from 'axios';

const GoogleCallback = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [error, setError] = useState(null);
  const googleCallbackUrl = `${process.env.REACT_APP_AUTH_SERVICE_BASE_URL}/auth/google/callback`;

  useEffect(() => {
    const handleGoogleCallback = async () => {
      const code = searchParams.get('code');

      if (!code) {
        setError('Missing authorization code.');
        return;
      }

      try {
        const response = await axios.get(
          `${googleCallbackUrl}?code=${code}`
        );

        const token = response.data.access_token;
        if (token) {
          localStorage.setItem('jwt_token', token);
          navigate('/dashboard');
        } else {
          setError('Login failed. No token received.');
        }
      } catch (err) {
        console.error('Google login error:', err);
        setError('Google login failed. Please try again.');
      }
    };

    handleGoogleCallback();
  }, [searchParams, navigate]);

  return (
    <div>
      <h2>Logging in with Google...</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
};

export default GoogleCallback;
