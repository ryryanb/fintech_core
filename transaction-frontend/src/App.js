// src/App.js
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import PayPalCheckout from './components/PayPalCheckout';
import StripeCheckout from './components/StripeCheckout';
import Login from './components/Login';
import ProtectedRoute from './components/ProtectedRoute';
import GoogleCallback from './components/GoogleCallback';
import Dashboard from './components/Dashboard';
import './App.css';

function App() {
  return (
    <div className="app-container">
      <Routes>
        <Route path="/google/callback" element={<GoogleCallback />} />
        <Route path="/" element={<Login />} />
        <Route 
          path="/payment-success" 
          element={
            <div className="success-message">
              <h2>Payment successful! 🎉</h2>
              <p>Thank you for your payment. Your transaction has been completed successfully.</p>
            </div>
          } 
        />
        <Route 
          path="/payment-cancel" 
          element={
            <div className="cancel-message">
              <h2>Payment canceled</h2>
              <p>Your payment has been canceled. No charges were made.</p>
            </div>
          } 
        />
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
        />
      </Routes>
    </div>
  );
}

export default App;
