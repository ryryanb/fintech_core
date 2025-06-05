import { loadStripe } from '@stripe/stripe-js';
import axios from 'axios';

const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLIC_KEY);

export default function StripeCheckout() {
  const createSession = async () => {
    try {
      const response = await axios.post(
        `${process.env.REACT_APP_API_URL}/api/stripe/create-checkout-session`,
        null,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`, // if needed
          },
        }
      );
      const stripe = await stripePromise;
      await stripe.redirectToCheckout({ sessionId: response.data.id });
    } catch (err) {
      console.error('Error creating Stripe session:', err);
    }
  };

  return (
    <div>
      <h3>Stripe Checkout</h3>
      <button onClick={createSession}>Pay with Stripe</button>
    </div>
  );
}
