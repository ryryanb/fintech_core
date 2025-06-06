import React, { useState } from 'react';
import PayPalCheckout from './PayPalCheckout';
import StripeCheckout from './StripeCheckout';
import './Dashboard.css';

const Dashboard = () => {
  const [selectedMethod, setSelectedMethod] = useState('stripe'); // Default to Stripe

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1>Transaction Payment Portal</h1>
        <div className="payment-toggle">
          <button
            className={`toggle-button ${selectedMethod === 'stripe' ? 'active' : ''}`}
            onClick={() => setSelectedMethod('stripe')}
          >
            <img 
              src="https://upload.wikimedia.org/wikipedia/commons/b/ba/Stripe_Logo%2C_revised_2016.svg" 
              alt="Stripe"
              className="payment-icon"
            />
            Stripe
          </button>
          <button
            className={`toggle-button ${selectedMethod === 'paypal' ? 'active' : ''}`}
            onClick={() => setSelectedMethod('paypal')}
          >
            <img 
              src="https://upload.wikimedia.org/wikipedia/commons/b/b5/PayPal.svg" 
              alt="PayPal"
              className="payment-icon"
            />
            PayPal
          </button>
        </div>
      </div>
      
      <div className="payment-container">
        {selectedMethod === 'stripe' ? (
          <div className="payment-method-container fade-in">
            <StripeCheckout />
          </div>
        ) : (
          <div className="payment-method-container fade-in">
            <PayPalCheckout />
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard; 