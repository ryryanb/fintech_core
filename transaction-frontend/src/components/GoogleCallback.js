import React, { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import './GoogleCallback.css';

const GoogleCallback = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [error, setError] = useState(null);

  useEffect(() => {
    const token = searchParams.get('token');
    const error = searchParams.get('error');

    if (error) {
      setError(decodeURIComponent(error));
      setTimeout(() => navigate('/'), 3000); // Redirect to login after 3 seconds
      return;
    }

    if (token) {
      localStorage.setItem('jwt_token', token);
      navigate('/dashboard');
    } else {
      setError('No authentication token received');
      setTimeout(() => navigate('/'), 3000);
    }
  }, [searchParams, navigate]);

  return (
    <div className="google-callback">
      <div className="callback-container">
        {error ? (
          <div className="error-message">
            <h2>Authentication Failed</h2>
            <p>{error}</p>
            <p>Redirecting to login page...</p>
          </div>
        ) : (
          <div className="loading-message">
            <h2>Completing Authentication</h2>
            <p>Please wait while we complete your sign-in...</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default GoogleCallback;
