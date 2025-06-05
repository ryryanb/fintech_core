# FinTech Core

FinTech Core is a comprehensive microservices-based platform designed to provide essential financial technology infrastructure for startups. It offers a robust foundation for building fintech applications with features including authentication, transaction processing, and payment integration.

Live demo:
Frontend: https://transaction-frontend-three.vercel.app

Auth service: https://fintech-core-auth-service.vercel.app/docs

Transaction service: https://transaction-service-dd9l.onrender.com

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

