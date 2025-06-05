import axios from 'axios';
import { useState } from 'react';

export default function PayPalCheckout() {
  const [amount, setAmount] = useState('');
  const [currency, setCurrency] = useState('USD');

  const createOrder = async () => {
    try {
      const response = await axios.post(
        `${process.env.REACT_APP_API_URL}/api/paypal/create-order`,
        null,
        {
          params: { currency, amount },
          headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt_token')}`, // if needed
          },
        }
      );
      const approveLink = response.data.links.find(link => link.rel === 'approve');
      if (approveLink) {
        window.location.href = approveLink.href;
      } else {
        alert('No approve link found');
      }
    } catch (err) {
      console.error('Error creating PayPal order:', err);
    }
  };

  return (
    <div>
      <h3>PayPal Checkout</h3>
      <input
        type="text"
        placeholder="Amount"
        value={amount}
        onChange={e => setAmount(e.target.value)}
      />
      <button onClick={createOrder}>Pay with PayPal</button>
    </div>
  );
}
