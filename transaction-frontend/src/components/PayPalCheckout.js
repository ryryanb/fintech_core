import axios from 'axios';
import { useState } from 'react';
import './PayPalCheckout.css';

export default function PayPalCheckout() {
  const [amount, setAmount] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [currency, setCurrency] = useState('USD');

  // Supported PayPal currencies
  const currencies = [
    { code: 'USD', symbol: '$' },
    { code: 'EUR', symbol: '€' },
    { code: 'GBP', symbol: '£' },
    { code: 'CAD', symbol: 'C$' },
    { code: 'AUD', symbol: 'A$' },
    { code: 'JPY', symbol: '¥' }
  ];

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

  const getCurrencySymbol = (currencyCode) => {
    const currency = currencies.find(c => c.code === currencyCode);
    return currency ? currency.symbol : currencyCode;
  };

  return (
    <div className="paypal-checkout">
      <h3>PayPal Checkout</h3>
      <div className="paypal-form">
        <div className="form-group">
          <label htmlFor="amount">Amount</label>
          <div className="amount-input-group">
            <input
              type="number"
              id="amount"
              className="amount-input"
              placeholder={`Enter amount in ${currency}`}
              value={amount}
              onChange={e => {
                setAmount(e.target.value);
                setError('');
              }}
              min="0"
              step="0.01"
            />
            <select
              value={currency}
              onChange={e => setCurrency(e.target.value)}
              className="currency-select"
            >
              {currencies.map(curr => (
                <option key={curr.code} value={curr.code}>
                  {curr.code}
                </option>
              ))}
            </select>
          </div>
        </div>

        {error && <div className="error-message">{error}</div>}
        
        <button
          className={`paypal-button ${loading ? 'loading' : ''}`}
          onClick={createOrder}
          disabled={loading}
        >
          {loading ? 'Processing...' : `Pay ${getCurrencySymbol(currency)}${amount || ''} with PayPal`}
        </button>
      </div>
    </div>
  );
}
