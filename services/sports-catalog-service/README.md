# sports-catalog-service

Microservice responsible for managing the sports catalog within the Open Sports Stats platform.

This service is part of the `sports-stats-app` monorepo. The repository root is not a Spring Boot application; this service is an independent Spring Boot application located under `services/sports-catalog-service`.

## Tech stack

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- Flyway
- Docker Compose
- Spring Boot Actuator

## Run locally

From this directory:

```bash
./mvnw spring-boot:run
```

Health check:

```http
GET /actuator/health
```

## Run tests

From this directory:

```bash
./mvnw clean test
```

## Sports API

The first catalog aggregate is `Sport`. A sport has an identifier, name, and active flag.

Available endpoints:

```http
POST /sports
GET /sports/{id}
GET /sports
```

Create request example:

```json
{
  "name": "Football"
}
```

Sports are created active by default. Blank names are rejected by the domain using custom sport exceptions.

## Architecture

The service follows a hexagonal architecture approach, separating domain, application and infrastructure concerns.

Current base package structure:

```text
com.pabalvrz.sportsstatsapp
|-- domain
|   |-- event
|   |-- exception
|   |-- model
|   |-- ports
|   |   |-- in
|   |   `-- out
|   `-- service
|-- application
|   |-- exception
|   `-- usecases
`-- infrastructure
    `-- adapters
        |-- input
        |   `-- rest
        |       `-- controller
        `-- output
            `-- persistence
                |-- entity
                |-- mapper
                `-- repository
```

## Package responsibilities

### domain

Contains the business model and pure domain concepts.

Expected responsibilities:

- Domain models
- Domain events
- Input and output ports
- Domain services

The domain layer should not depend on Spring, JPA, REST APIs or infrastructure details.

### application

Contains application use cases.

Expected responsibilities:

- Orchestrating domain logic
- Defining application-level flows
- Implementing domain use case contracts

### infrastructure

Contains technical adapters and framework-specific implementations.

Expected responsibilities:

- REST controllers
- Persistence entities
- Spring Data repositories
- Persistence mappers
- JPA adapters
- Framework configuration when needed
