import { loadStripe } from '@stripe/stripe-js';
import axios from 'axios';
import { useState } from 'react';
import './StripeCheckout.css';

const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLIC_KEY);

export default function StripeCheckout() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [amount, setAmount] = useState('');
  const [currency, setCurrency] = useState('usd');
  const [productName, setProductName] = useState('');

  const createSession = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    // Convert amount to cents
    const amountInCents = Math.round(parseFloat(amount) * 100);

    if (isNaN(amountInCents) || amountInCents <= 0) {
      setError('Please enter a valid amount');
      setLoading(false);
      return;
    }

    try {
      const response = await axios.post(
        `${process.env.REACT_APP_API_URL}/api/stripe/create-checkout-session`,
        {
          amount: amountInCents,
          currency: currency,
          productName: productName || 'Custom Payment'
        },
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
      setError(err.response?.data?.error || 'Failed to initialize Stripe checkout. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="stripe-checkout">
      <h3>Stripe Checkout</h3>
      <form onSubmit={createSession} className="stripe-form">
        {error && <div className="error-message">{error}</div>}
        
        <div className="form-group">
          <label htmlFor="amount">Amount</label>
          <div className="amount-input">
            <input
              type="number"
              id="amount"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              placeholder="Enter amount"
              step="0.01"
              min="0.50"
              required
              className="amount-field"
            />
            <select
              value={currency}
              onChange={(e) => setCurrency(e.target.value)}
              className="currency-select"
            >
              <option value="usd">USD</option>
              <option value="eur">EUR</option>
              <option value="gbp">GBP</option>
            </select>
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="productName">Description (optional)</label>
          <input
            type="text"
            id="productName"
            value={productName}
            onChange={(e) => setProductName(e.target.value)}
            placeholder="Enter payment description"
            className="product-name-field"
          />
        </div>

        <button
          type="submit"
          className={`stripe-button ${loading ? 'loading' : ''}`}
          disabled={loading}
        >
          {loading ? 'Initializing...' : `Pay ${amount ? `$${amount}` : ''} with Stripe`}
        </button>
      </form>
    </div>
  );
}
