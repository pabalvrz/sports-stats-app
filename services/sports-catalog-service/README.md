cat > services/sports-catalog-service/README.md <<'EOF'
# sports-catalog-service

Microservice responsible for managing the sports catalog within the Open Sports Stats platform.

This service is part of the `sports-stats-app` monorepo. The repository root is not a Spring Boot application; this service is an independent Spring Boot application located under `services/sports-catalog-service`.

## Tech stack

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- Docker Compose
- Spring Boot Actuator

## Run locally

From this directory:

```bash
./mvnw spring-boot:run
```

Health check:

```bash
GET /actuator/health
```

## Run tests

From this directory:

```bash
./mvnw clean test
```

## Architecture

The service follows a hexagonal architecture approach, separating domain, application and infrastructure concerns.

Current base package structure:

```text
com.pabalvrz.sportsstatsapp
├── domain
│   ├── event
│   ├── model
│   ├── repository
│   └── service
├── application
│   └── usecase
└── infrastructure
    └── adapters
        ├── input
        │   └── rest
        │       └── controller
        └── output
            └── persistence
                ├── entity
                ├── mapper
                └── repository
```

## Package responsibilities

### domain

Contains the business model and pure domain concepts.

Expected responsibilities:

- Domain models
- Domain events
- Domain repository contracts
- Domain services

The domain layer should not depend on Spring, JPA, REST APIs or infrastructure details.

### application

Contains application use cases.

Expected responsibilities:

- Orchestrating domain logic
- Defining application-level flows
- Coordinating input and output boundaries through use cases

### infrastructure

Contains technical adapters and framework-specific implementations.

Expected responsibilities:

- REST controllers
- Persistence entities
- Spring Data repositories
- Persistence mappers
- JPA adapters
- Framework configuration when needed

## Notes

This README documents the initial architectural structure only. Functional domain classes, use cases, REST endpoints and persistence implementations will be added in future features.