import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [tenantId, setTenantId] = useState(0);  // Default to tenant_id 0
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const loginUrl = `${process.env.REACT_APP_AUTH_SERVICE_BASE_URL}/login`;

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
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
          },
        }
      );

      localStorage.setItem("jwt_token", response.data.access_token);
      navigate('/dashboard');
    } catch (err) {
      setError("Invalid credentials. Please try again.");
    }
  };

  const handleGoogleLogin = async () => {
    try {
      const res = await fetch("https://saas-auth-system.vercel.app/auth/google/login");
      const data = await res.json();
      if (data.auth_url) {
        window.location.href = data.auth_url;  // Redirect to Google login
      } else {
        setError("Failed to initiate Google login.");
      }
    } catch (err) {
      console.error(err);
      setError("Google login error. Try again later.");
    }
  };

  return (
    <div className="login-container">
      <h2>Login</h2>

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
