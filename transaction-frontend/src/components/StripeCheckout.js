import { loadStripe } from '@stripe/stripe-js';
import axios from 'axios';
import { useState } from 'react';
import './StripeCheckout.css';

const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLIC_KEY);

export default function StripeCheckout() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const createSession = async () => {
    setLoading(true);
    setError('');

    try {
      const response = await axios.post(
        `${process.env.REACT_APP_API_URL}/api/stripe/create-checkout-session`,
        null,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt_token')}`,
          },
        }
      );
      const stripe = await stripePromise;
      const { error } = await stripe.redirectToCheckout({ 
        sessionId: response.data.id 
      });
      
      if (error) {
        setError(error.message);
      }
    } catch (err) {
      console.error('Error creating Stripe session:', err);
      setError('Failed to initialize Stripe checkout. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="stripe-checkout">
      <h3>Stripe Checkout</h3>
      <div className="stripe-form">
        {error && <div className="error-message">{error}</div>}
        <button
          className={`stripe-button ${loading ? 'loading' : ''}`}
          onClick={createSession}
          disabled={loading}
        >
          {loading ? 'Initializing...' : 'Pay with Stripe'}
        </button>
      </div>
    </div>
  );
}
