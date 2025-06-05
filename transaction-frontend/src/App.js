// src/App.js
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import PayPalCheckout from './components/PayPalCheckout';
import StripeCheckout from './components/StripeCheckout';
import Login from './components/Login';
import ProtectedRoute from './components/ProtectedRoute';
import GoogleCallback from './components/GoogleCallback';

function App() {
  return (
    <div style={{ padding: '2rem' }}>
      <Routes>
        <Route path="/google/callback" element={<GoogleCallback />} />
        <Route path="/" element={<Login />} />
        <Route path="/payment-success" element={<h2>Payment successful! 🎉</h2>} />
        <Route path="/payment-cancel" element={<h2>Payment canceled.</h2>} />
        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <>
                <h1>Transaction Payment Portal</h1>
                <PayPalCheckout />
                <hr />
                <StripeCheckout />
              </>
            </ProtectedRoute>
          }
        />
      </Routes>
    </div>
  );
}

export default App;
