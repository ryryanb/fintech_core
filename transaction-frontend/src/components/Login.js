import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [tenantId, setTenantId] = useState(0);  // Default to tenant_id 0
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  
  // Add fallback URL and logging
  const authServiceBaseUrl = process.env.REACT_APP_AUTH_SERVICE_BASE_URL;
  const loginUrl = `${authServiceBaseUrl}/login`;

  // Log the configuration on component mount
  useEffect(() => {
    console.log('Auth Service Base URL:', authServiceBaseUrl);
  }, [authServiceBaseUrl]);

  const handleLogin = async (e) => {
    e.preventDefault();
    console.log('Attempting login to:', loginUrl);

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
          withCredentials: true // Important for CORS with credentials
        }
      );

      console.log('Login response:', response);
      localStorage.setItem("jwt_token", response.data.access_token);
      navigate('/dashboard');
    } catch (err) {
      console.error('Login error:', err);
      if (err.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        console.error('Error response:', err.response.data);
        console.error('Error status:', err.response.status);
        console.error('Error headers:', err.response.headers);
        setError(`Server error: ${err.response.data.detail || 'Unknown error'}`);
      } else if (err.request) {
        // The request was made but no response was received
        console.error('No response received:', err.request);
        setError('No response from server. Please try again later.');
      } else {
        // Something happened in setting up the request that triggered an Error
        console.error('Error setting up request:', err.message);
        setError(err.message || "Invalid credentials. Please try again.");
      }
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
        window.location.href = data.auth_url;  // Redirect to Google login
      } else {
        setError("Failed to initiate Google login.");
      }
    } catch (err) {
      console.error('Google login error:', err);
      setError("Google login error. Try again later.");
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>
      {!authServiceBaseUrl && (
        <div style={{ color: 'red', marginBottom: '1rem' }}>
          Warning: Authentication service URL is not configured
        </div>
      )}

      <form onSubmit={handleLogin}>
        <div>
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>

        <div>
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <div>
          <label htmlFor="tenant_id">Tenant ID</label>
          <input
            type="number"
            id="tenant_id"
            value={tenantId}
            onChange={(e) => setTenantId(Number(e.target.value))}
            required
          />
        </div>

        {error && <p className="error">{error}</p>}
        <button type="submit">Login</button>
      </form>

      <hr />

      <button onClick={handleGoogleLogin}>Sign in with Google</button>
    </div>
  );
};

export default Login;
