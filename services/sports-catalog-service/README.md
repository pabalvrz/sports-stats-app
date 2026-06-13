# sports-catalog-service

Microservice responsible for managing the sports catalog within the Open Sports Stats platform.

This service is part of the `sports-stats-app` monorepo. The repository root is not a Spring Boot application; this service is an independent Spring Boot application located under `services/sports-catalog-service`.

## Tech Stack

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- Flyway
- Docker Compose
- Spring Boot Actuator

## Run Locally

From this directory:

```bash
./mvnw spring-boot:run
```

Health check:

```http
GET /actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

## Run Tests

From this directory:

```bash
./mvnw clean test
```

## Sports API

A `Sport` has an identifier, name, and active flag.

Sports are created active by default. Blank names are rejected by validation/domain rules and returned as `400 Bad Request`. Missing sports are returned as `404 Not Found`.

Available endpoints:

```http
POST /sports
GET /sports
GET /sports/{id}
GET /sports/by-name/{name}
PUT /sports/{id}
```

### Create Sport

```http
POST /sports
Content-Type: application/json
```

Request:

```json
{
  "name": "Football"
}
```

Response:

```http
201 Created
Location: /sports/{id}
```

```json
{
  "id": "7d912d34-0a0d-4a21-a833-9b6f54f6d91d",
  "name": "Football",
  "active": true
}
```

### List Sports

```http
GET /sports
```

Response:

```json
[
  {
    "id": "7d912d34-0a0d-4a21-a833-9b6f54f6d91d",
    "name": "Football",
    "active": true
  },
  {
    "id": "b325ab0e-25ec-49c5-9987-a41e8d82a3ab",
    "name": "Tennis",
    "active": true
  }
]
```

### Get Sport By Id

```http
GET /sports/{id}
```

Response:

```json
{
  "id": "7d912d34-0a0d-4a21-a833-9b6f54f6d91d",
  "name": "Football",
  "active": true
}
```

### Get Sport By Name

```http
GET /sports/by-name/{name}
```

Example:

```http
GET /sports/by-name/Football
```

Response:

```json
{
  "id": "7d912d34-0a0d-4a21-a833-9b6f54f6d91d",
  "name": "Football",
  "active": true
}
```

### Update Sport

```http
PUT /sports/{id}
Content-Type: application/json
```

Request:

```json
{
  "name": "Association Football"
}
```

Response:

```json
{
  "id": "7d912d34-0a0d-4a21-a833-9b6f54f6d91d",
  "name": "Association Football",
  "active": true
}
```

## Error Responses

Invalid request body:

```http
400 Bad Request
```

```json
{
  "title": "Invalid request body",
  "detail": "name: name must not be blank"
}
```

Missing sport:

```http
404 Not Found
```

```json
{
  "title": "Sport not found",
  "detail": "Sport not found: Football"
}
```

## Architecture

The service follows a hexagonal architecture approach, separating domain, application and infrastructure concerns.

Current base package structure:

```text
com.pabalvrz.sportsstatsapp
|-- domain
|   |-- exception
|   |-- model
|   `-- ports
|       |-- in
|       `-- out
|-- application
|   |-- command
|   |   |-- Command.java
|   |   |-- CommandBus.java
|   |   |-- CommandHandler.java
|   |   |-- SimpleCommandBus.java
|   |   |-- create
|   |   `-- update
|   |-- exception
|   |-- result
|   `-- query
|       |-- Query.java
|       |-- QueryBus.java
|       |-- QueryHandler.java
|       |-- SimpleQueryBus.java
|       |-- find
|       `-- list
`-- infrastructure
    `-- adapters
        |-- input
        |   `-- rest
        |       |-- controller
        |       |-- request
        |       `-- response
        `-- output
            `-- persistence
                |-- entity
                |-- mapper
                `-- repository
```

## Package Responsibilities

### domain

Contains the business model and pure domain concepts.

Responsibilities:

- Domain models
- Input ports / use case contracts
- Output ports
- Domain exceptions

The domain layer should not depend on Spring, JPA, REST APIs or infrastructure details.

### application

Contains the internal CQRS application model: commands, queries, handlers, and synchronous buses.

Responsibilities:

- Defining command objects for state-changing operations
- Defining query objects for read-only operations
- Dispatching commands through `CommandBus`
- Dispatching queries through `QueryBus`
- Handling each command/query in a dedicated handler
- Implementing the domain input ports / use case contracts
- Returning application result DTOs to REST adapters
- Orchestrating domain logic
- Defining application-level flows
- Translating missing resources into application exceptions

Commands currently cover creating and updating sports. Queries currently cover listing sports, finding a sport by id, and finding a sport by name.

The command bus is explicit and type-safe for the current write operations instead of using a dynamic handler registry. The query bus is a simple in-process dispatcher backed by Spring-managed handlers. Both return application result DTOs, so REST controllers do not expose or map domain models directly. This design does not introduce event sourcing, messaging, async processing, separate read databases, or separate read models.

### infrastructure

Contains technical adapters and framework-specific implementations.

Responsibilities:

- REST controllers
- Request and response DTOs
- Persistence entities
- Spring Data repositories
- Persistence mappers
- JPA adapters
- Framework configuration when needed
