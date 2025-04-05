
# SaaS Auth System

This project is a **SaaS Authentication System** built using **Spring Boot** for the backend and **FastAPI** for authentication. The application is dockerized for easy deployment and uses **PostgreSQL** as the database.

## Table of Contents

1. [Introduction](#introduction)
2. [Technologies Used](#technologies-used)
3. [Prerequisites](#prerequisites)
4. [Getting Started](#getting-started)
5. [Project Structure](#project-structure)
6. [Environment Variables](#environment-variables)
7. [Docker Setup](#docker-setup)
8. [Troubleshooting](#troubleshooting)
9. [Contributing](#contributing)
10. [License](#license)

## Introduction

This project provides a basic authentication system for a SaaS application. It includes two main components:

- **Spring Boot App**: Handles the main business logic and services.
- **FastAPI App**: Manages the authentication processes such as login, registration, and token management.

Both services are configured to run in **Docker** containers, and PostgreSQL is used for storing user data and authentication tokens.

## Technologies Used

- **Spring Boot**: Backend for managing user accounts, authorization, and business logic.
- **FastAPI**: Fast API server for handling authentication.
- **PostgreSQL**: Database for storing user data and authentication tokens.
- **Docker**: Containerization of the application for easy deployment.

## Prerequisites

Before running this project, make sure you have the following installed:

1. **Docker**: [Install Docker](https://www.docker.com/get-started)
2. **Docker Compose**: [Install Docker Compose](https://docs.docker.com/compose/install/)
3. **Git**: [Install Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
4. **Python (for FastAPI)**: [Install Python](https://www.python.org/downloads/)
5. **Java (for Spring Boot)**: [Install OpenJDK 17](https://adoptopenjdk.net/)

## Getting Started

Follow these steps to set up the project locally:

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/saas-auth-system.git
   cd saas-auth-system
   ```

2. Set up the environment variables by creating a `.env` file in the root of the project (see the [Environment Variables](#environment-variables) section for more details).

3. Build and run the application using Docker Compose:
   ```bash
   docker-compose up --build
   ```

4. Once the services are up, you can access:

   - **FastAPI Auth Service**: [http://localhost:8000](http://localhost:8000)
   - **Spring Boot App**: [http://localhost:8080](http://localhost:8080)
   - **PostgreSQL Database**: Available to the backend via Docker network.

5. The FastAPI service and Spring Boot application will start in separate containers, and you can view logs using:
   ```bash
   docker-compose logs -f
   ```

6. To shut down the services, run:
   ```bash
   docker-compose down
   ```

## Project Structure

Here’s an overview of the project structure:

```
saas-auth-system/
│
├── backend/                    # Spring Boot App
│   ├── src/                    # Source code for the Spring Boot backend
│   └── Dockerfile               # Dockerfile for Spring Boot app
│
├── auth/                       # FastAPI Auth service
│   ├── app/                     # FastAPI codebase
│   ├── Dockerfile               # Dockerfile for FastAPI app
│   └── requirements.txt         # Python dependencies
│
├── .env                         # Environment variables (ignore in version control)
├── docker-compose.yml           # Docker Compose configuration
├── .gitignore                   # Git ignore configuration
└── README.md                    # Project documentation
```

## Environment Variables

Create a `.env` file in the root directory with the following variables (replace the values with your own settings):

```bash
# PostgreSQL database
DB_HOST=postgres
DB_PORT=5432
DB_NAME=saas_db
DB_USER=yourusername
DB_PASSWORD=yourpassword

# FastAPI and Spring Boot settings
FASTAPI_HOST=fastapi-auth
FASTAPI_PORT=8000
SPRING_BOOT_HOST=spring-boot-app
SPRING_BOOT_PORT=8080

# JWT Secrets and configurations
JWT_SECRET_KEY=your_jwt_secret_key
JWT_ALGORITHM=HS256
```

**Note**: You should ensure that the `.env` file is added to `.gitignore` to prevent it from being pushed to your version control system.

## Docker Setup

The project uses **Docker Compose** to manage multiple containers, including:

- **PostgreSQL**: Database for storing authentication data.
- **FastAPI**: Authentication API server.
- **Spring Boot**: Backend API server.

To set up the project, simply use the `docker-compose` command:

### Build and Run the Containers

```bash
docker-compose up --build
```

This will pull the necessary Docker images (if they are not available locally), build the containers, and start the services.

### Stopping the Services

To stop the services, run:

```bash
docker-compose down
```

This will stop and remove all running containers.

## Troubleshooting

### Common Issues

1. **FastAPI not starting**: 
   - Ensure that the `.env` file is correctly set up and all environment variables are correct.
   - Check the FastAPI container logs for detailed error messages:
     ```bash
     docker-compose logs -f fastapi-auth
     ```

2. **Database connection issues**:
   - Verify the database credentials in your `.env` file.
   - Check if PostgreSQL is running properly by inspecting the logs:
     ```bash
     docker-compose logs -f postgres
     ```

3. **Missing Docker images**:
   - Run `docker-compose pull` to pull the latest images if you encounter issues with missing images.

## Contributing

We welcome contributions to this project. If you have ideas for improvements or bug fixes, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or fix.
3. Make your changes and commit them.
4. Push your changes to your forked repository.
5. Submit a pull request.

Please ensure that your code passes all tests before submitting a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

