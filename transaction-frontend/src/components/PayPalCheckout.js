import axios from 'axios';
import { useState } from 'react';
import './PayPalCheckout.css';

export default function PayPalCheckout() {
  const [amount, setAmount] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const currency = 'USD';

  const createOrder = async () => {
    if (!amount || isNaN(amount) || parseFloat(amount) <= 0) {
      setError('Please enter a valid amount');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const response = await axios.post(
        `${process.env.REACT_APP_API_URL}/api/paypal/create-order`,
        null,
        {
          params: { currency, amount },
          headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt_token')}`,
          },
        }
      );
      const approveLink = response.data.links.find(link => link.rel === 'approve');
      if (approveLink) {
        window.location.href = approveLink.href;
      } else {
        setError('No approve link found');
      }
    } catch (err) {
      console.error('Error creating PayPal order:', err);
      setError('Failed to create PayPal order. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="paypal-checkout">
      <h3>PayPal Checkout</h3>
      <div className="paypal-form">
        <div className="currency-display">
          <span>Currency: {currency}</span>
        </div>
        <input
          type="number"
          className="amount-input"
          placeholder="Enter amount"
          value={amount}
          onChange={e => {
            setAmount(e.target.value);
            setError('');
          }}
          min="0"
          step="0.01"
        />
        {error && <div className="error-message">{error}</div>}
        <button
          className={`paypal-button ${loading ? 'loading' : ''}`}
          onClick={createOrder}
          disabled={loading}
        >
          {loading ? 'Processing...' : 'Pay with PayPal'}
        </button>
      </div>
    </div>
  );
}
