
# Transaction Service

The **Transaction Service** is a Spring Boot-based application designed to process financial transactions securely. It allows users to initiate payments, check transaction statuses, and integrates with external payment gateways (such as Stripe) for secure payment processing.

## User Story
As a user, I want to process financial transactions securely so that I can perform payments. This service ensures smooth payment processing and provides secure transaction management.

## Features

- **Secure Payment Processing**: Interacts with external payment gateways (e.g., Stripe) for payment processing.
- **Transaction Management**: Manages transaction details like the amount, status, and payment method.
- **REST API**: Exposes endpoints for initiating payments and retrieving transaction information.
- **Input Validation**: Ensures valid data is provided before proceeding with payment processing.

## Technologies

- **Spring Boot**: Framework for building the service.
- **Spring Security**: Ensures secure access to the transaction service.
- **Stripe API**: Used for processing payments (can be extended for other gateways like PayPal).
- **JPA / Hibernate**: For database interaction and persistence.
- **PostgreSQL**: Database for storing transaction details.

## Getting Started

### Prerequisites

- JDK 17+
- Maven
- Docker (optional, for Docker Compose setup)

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/transaction-service.git
cd transaction-service
```

### 2. Configure Application Properties

Set up database configurations and API keys for Stripe (or any other payment gateway) in `src/main/resources/application.properties`.

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/transaction_db
spring.datasource.username=your-db-username
spring.datasource.password=your-db-password

# Stripe API Configuration
stripe.api.key=your_stripe_api_key
```

### 3. Build and Run the Application

Build the project using Maven:

```bash
mvn clean install
```

Then run the Spring Boot application:

```bash
mvn spring-boot:run
```

### 4. Docker (Optional)

You can run the application with Docker Compose for local development. A `docker-compose.yml` file is provided to set up both PostgreSQL and the Spring Boot application.

```bash
docker-compose up --build
```

## API Endpoints

### 1. POST /payment/process-payment

This endpoint initiates a payment with the provided transaction details and payment method.

- **Request Body**: Transaction details such as payment id, sender account, receiver account, amount, and payment method.
Sample:
```
{
    "amount": 100.00,
    "senderAccount": "sender_account_123",
    "receiverAccount": "merchant_account_456",
    "paymentMethod": "tok_visa",
    "timestamp": "2025-04-05T12:30:00",
    "currencyType": "USD",
    "paymentId": "pay_id_12348"
}
```
- **Response**: A created transaction object with a status of `PENDING`, `COMPLETED`, or `FAILED`.

### 2. GET /transactions/{id}

Retrieves the transaction details for a specific transaction by its ID.

- **Response**: The transaction details, including the transaction status and other metadata.



## Integration with Payment Gateways

The service integrates with external payment gateways, such as **Stripe**. You'll need to set up your API keys for payment processing. The payment processing logic can be extended to support other gateways like PayPal.

## Database

The service uses a PostgreSQL database to store transaction data. The transaction entity includes information like:

- `userId`: The user initiating the payment.
- `amount`: The amount to be processed.
- `status`: The transaction status (e.g., `PENDING`, `COMPLETED`).
- `paymentMethod`: The payment method used (e.g., Stripe token).
- `createdAt`: Timestamp of transaction creation.
- `updatedAt`: Timestamp of the last transaction update.

## Running Tests

To run tests, use the following command:

```bash
mvn test
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
