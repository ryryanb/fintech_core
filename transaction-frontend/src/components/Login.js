import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';
import './Login.css';

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const tenantId = 0; // Default tenant ID as hidden field
  
  const authServiceBaseUrl = process.env.REACT_APP_AUTH_SERVICE_BASE_URL || 'http://localhost:5001';
  const loginUrl = `${authServiceBaseUrl}/login`;

  useEffect(() => {
    console.log('Auth Service Base URL:', authServiceBaseUrl);
  }, [authServiceBaseUrl]);

  const handleLogin = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError(null);
    
    console.log('Login attempt:', {
      url: loginUrl,
      email,
      tenant_id: tenantId,
      // Don't log password for security
    });

    try {
      if (!authServiceBaseUrl) {
        throw new Error('Authentication service URL is not configured');
      }

      const response = await axios.post(
        loginUrl,
        {
          email,
          password,
          tenant_id: tenantId,
        },
        {
          headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
          },
          withCredentials: true
        }
      );

      console.log('Login successful:', {
        status: response.status,
        headers: response.headers,
      });

      if (response.data && response.data.access_token) {
        localStorage.setItem("jwt_token", response.data.access_token);
        navigate('/dashboard');
      } else {
        throw new Error('No access token received');
      }
    } catch (err) {
      console.error('Login error details:', {
        message: err.message,
        response: err.response ? {
          status: err.response.status,
          data: err.response.data,
          headers: err.response.headers
        } : 'No response',
        request: err.request ? 'Request was made but no response received' : 'Request setup failed'
      });

      if (err.response) {
        // Server responded with error
        const errorMessage = err.response.data?.detail || err.response.data?.message || 'Invalid credentials';
        setError(`Error: ${errorMessage}`);
      } else if (err.request) {
        // Request made but no response
        setError('Unable to reach the authentication server. Please try again later.');
      } else {
        // Error setting up request
        setError(err.message || "An unexpected error occurred. Please try again.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleGoogleLogin = async () => {
    try {
      if (!authServiceBaseUrl) {
        throw new Error('Authentication service URL is not configured');
      }

      const res = await fetch(`${authServiceBaseUrl}/auth/google/login`);
      const data = await res.json();
      if (data.auth_url) {
        window.location.href = data.auth_url;
      } else {
        setError("Failed to initiate Google login.");
      }
    } catch (err) {
      console.error('Google login error:', err);
      setError("Google login error. Try again later.");
    }
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <div className="login-header">
          <h1>Welcome Back</h1>
          <p>Please sign in to continue</p>
        </div>

        {!authServiceBaseUrl && (
          <div className="warning-message">
            Warning: Authentication service URL is not configured
          </div>
        )}

        <form onSubmit={handleLogin} className="login-form">
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              placeholder="Enter your email"
              className="form-input"
              disabled={isLoading}
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              placeholder="Enter your password"
              className="form-input"
              disabled={isLoading}
            />
          </div>

          {error && <div className="error-message">{error}</div>}
          
          <button 
            type="submit" 
            className="login-button"
            disabled={isLoading}
          >
            {isLoading ? 'Signing in...' : 'Sign In'}
          </button>

          <div className="divider">
            <span>OR</span>
          </div>

          <button 
            type="button" 
            onClick={handleGoogleLogin}
            className="google-button"
            disabled={isLoading}
          >
            <img 
              src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg" 
              alt="Google Logo"
              className="google-icon"
            />
            Sign in with Google
          </button>
        </form>
      </div>
    </div>
  );
};

export default Login;
