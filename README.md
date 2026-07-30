# FinTech Core

FinTech Core is a comprehensive microservices-based platform designed to provide essential financial technology infrastructure for startups. It offers a robust foundation for building fintech applications with features including authentication, transaction processing, and payment integration.

Live demo:
Frontend: https://fintech-core-frontend.vercel.app

Auth service: https://fintech-core-auth-service.vercel.app/docs

Transaction service: https://transaction-service-dd9l.onrender.com

## Payment Flow

Follow the steps below to test the payment integration.

### 1. Register a User

Navigate to the Auth Service API documentation and manually register a new user:

**https://fintech-core-auth-service.vercel.app/docs**

**Registration Request**

<img width="1399" height="670" alt="Register Request" src="https://github.com/user-attachments/assets/9b2e9017-6308-4f91-af5d-24a65bc1832d" />

**Registration Response**

<img width="1386" height="480" alt="Registration Response" src="https://github.com/user-attachments/assets/2dde902a-7534-4fdd-b4ca-d79bdebb9436" />

---

### 2. Sign In

Open the FinTech Core frontend and sign in using the credentials you registered in the previous step.

<img width="1227" height="711" alt="Login Screen" src="https://github.com/user-attachments/assets/ff0f53a6-88c7-4376-83d0-b8b80966b3f9" />

---

### 3. Select a Payment Provider

After a successful login, the payment portal is displayed. Choose either **Stripe** or **PayPal** to begin the checkout process.

<img width="1308" height="737" alt="Payment Portal" src="https://github.com/user-attachments/assets/87783b37-1798-4f35-9686-997495f0d7d0" />

---

### 4. Complete the PayPal Checkout

The screenshots below demonstrate a successful PayPal payment flow.

**PayPal Checkout**

<img width="1362" height="725" alt="PayPal Checkout" src="https://github.com/user-attachments/assets/97ba4115-489a-43db-bee9-b483e8baa3f2" />

**Payment Details**

<img width="1421" height="758" alt="PayPal Payment Details" src="https://github.com/user-attachments/assets/30b844f7-eaed-47cd-9fae-233ada04c591" />

---

### 5. Payment Successful

Once the payment has been completed successfully, the application redirects to the success page and displays a confirmation message.

<img width="1053" height="239" alt="Payment Success" src="https://github.com/user-attachments/assets/5bcf5259-e909-4984-8d6d-fde08d56fa28" />


## 🌟 Features

- **Authentication Service** (FastAPI)
  - JWT-based authentication
  - Multi-tenant support
  - Google OAuth integration
  - Role-based access control

- **Transaction Service** (Spring Boot)
  - Payment processing
  - Transaction history
  - Multiple payment gateway integration
  - Secure transaction handling

- **Payment Integration**
  - PayPal integration
  - Stripe integration
  - Extensible payment gateway architecture

- **Frontend Demo** (React)
  - Modern React-based UI
  - Responsive design
  - Secure authentication flow
  - Payment processing demonstration

## 🏗 Architecture

```
FinTech Core
├── fastapi-auth/           # Authentication microservice (FastAPI)
├── transaction-service/    # Transaction processing service (Spring Boot)
├── transaction-frontend/   # Demo frontend application (React)
└── docker-compose.yml     # Container orchestration
```

## 🚀 Getting Started

### Prerequisites

- Docker and Docker Compose
- PostgreSQL
- Node.js (for local frontend development)
- Java 17 (for local backend development)
- Python 3.9+ (for authentication service)

### Environment Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/fintech-core.git
cd fintech-core
```

2. Create a `.env` file in the root directory:
```env
# Database Configuration
DB_USER=your_db_user
DB_PASSWORD=your_db_password

# PayPal Configuration
PAYPAL_CLIENT_ID=your_paypal_client_id
PAYPAL_CLIENT_SECRET=your_paypal_client_secret

# Stripe Configuration
STRIPE_API_KEY=your_stripe_key

# JWT Configuration
JWT_SECRET=your_jwt_secret
```

3. Start the services:
```bash
docker-compose up
```

### Service Endpoints

- Authentication Service: http://localhost:5001
- Transaction Service: http://localhost:8086
- Frontend Application: http://localhost:3000

## 📚 API Documentation

### Authentication Service (FastAPI)
- POST `/login` - User authentication
- POST `/register` - User registration
- GET `/auth/google/login` - Google OAuth login
- GET `/auth/google/callback` - Google OAuth callback

### Transaction Service (Spring Boot)
- POST `/api/paypal/create-order` - Create PayPal order
- POST `/api/stripe/create-checkout-session` - Create Stripe session
- GET `/api/transactions` - Get transaction history
- POST `/api/payments` - Process payment

## 🔒 Security

- JWT-based authentication
- HTTPS encryption
- CORS configuration
- Role-based access control
- Secure payment processing

## 🧪 Testing

Each service includes its own test suite:

```bash
# Authentication Service
cd fastapi-auth
pytest

# Transaction Service
cd transaction-service
./mvnw test

# Frontend
cd transaction-frontend
npm test
```

## 🛠 Development

### Local Development Setup

1. Authentication Service:
```bash
cd fastapi-auth
python -m venv venv
source venv/bin/activate  # or `venv\Scripts\activate` on Windows
pip install -r requirements.txt
uvicorn app.main:app --reload
```

2. Transaction Service:
```bash
cd transaction-service
./mvnw spring-boot:run
```

3. Frontend:
```bash
cd transaction-frontend
npm install
npm start
```

## 📦 Deployment

The project is containerized and can be deployed using Docker Compose or Kubernetes.

### Docker Compose Deployment
```bash
docker-compose up -d
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- FastAPI for the authentication service
- Spring Boot for the transaction service
- React for the frontend implementation
- PayPal and Stripe for payment integration

## 📞 Support

For support, please open an issue in the GitHub repository or contact the maintainers.

