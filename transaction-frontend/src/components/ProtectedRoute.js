// src/components/ProtectedRoute.js
import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
  const token = localStorage.getItem('jwt_token');
  return token ? children : <Navigate to="/" replace />;
};

export default ProtectedRoute;
