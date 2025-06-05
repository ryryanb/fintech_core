# Kubernetes Configuration for FinTech Core

This directory contains Kubernetes manifests for deploying the FinTech Core platform.

## Directory Structure

```
k8s/
├── base/                   # Base configurations
│   ├── auth-service/      # FastAPI auth service
│   ├── transaction-service/# Spring Boot service
│   └── frontend/          # React frontend
├── overlays/              # Environment-specific configs
│   ├── development/
│   └── production/
```

## Prerequisites

- Kubernetes cluster (e.g., minikube, GKE, EKS, AKS)
- kubectl installed and configured
- kustomize installed (v4.0.0+)

## Configuration

### Base Configuration
The `base` directory contains the core Kubernetes manifests that are common across all environments:
- Deployments
- Services
- Resource limits
- Health checks

### Environment-Specific Configuration
The `overlays` directory contains environment-specific configurations:
- Development: Used for local/development environments
- Production: Used for production deployments

## Deployment

### Development
```bash
kubectl apply -k overlays/development
```

### Production
```bash
kubectl apply -k overlays/production
```

## Services

### Auth Service
- Port: 5000
- Endpoints: /login, /register, /auth/*
- Health check: /health

### Transaction Service
- Port: 8086
- Endpoints: /api/*
- Health check: /actuator/health

### Frontend
- Port: 80
- Static content served via nginx

## Security

Sensitive information (API keys, credentials) should be managed using Kubernetes Secrets:
```bash
kubectl create secret generic auth-service-secrets --from-literal=database-url='your-db-url'
kubectl create secret generic transaction-service-secrets \
  --from-literal=database-url='your-db-url' \
  --from-literal=paypal-client-id='your-paypal-id' \
  --from-literal=paypal-client-secret='your-paypal-secret' \
  --from-literal=stripe-api-key='your-stripe-key'
```

## Monitoring

Health checks are configured for all services:
- Readiness probes: Ensure services are ready to accept traffic
- Liveness probes: Ensure services are healthy and running

## Resource Management

Resource limits and requests are defined for all containers to ensure proper scheduling and resource allocation. 