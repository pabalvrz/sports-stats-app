# sports-stats-app

Open source microservices-based sports statistics platform built with Java, Spring Boot, PostgreSQL and Docker.

## Project Status

This project is in its early development phase.

The current focus is the first backend microservice: `sports-catalog-service`, which exposes the initial sports catalog API and establishes the project architecture for future services.

## Architecture Approach

This repository is organized as a monorepo.

Each backend service lives inside the `services/` directory as an independent Spring Boot application.

```txt
sports-stats-app/
|-- services/
|   `-- sports-catalog-service/
|-- .github/
|-- README.md
|-- CONTRIBUTING.md
`-- LICENSE
```

## Current Services

### sports-catalog-service

Spring Boot microservice responsible for the shared sports catalog domain.

The first supported aggregate is `Sport`, with endpoints to create, list, filter by active status, retrieve by identifier, retrieve by name, update, activate, and deactivate sports.

Current status:

* Spring Boot project created
* Java 21 configured
* Maven configured
* PostgreSQL local dependency prepared
* Flyway migration for the `sports` table
* Actuator health check available
* Sports REST API available

Sports API summary:

```http
POST /sports
GET /sports
GET /sports?active={true|false}
GET /sports/{id}
GET /sports/by-name/{name}
PUT /sports/{id}
PATCH /sports/{id}/activate
PATCH /sports/{id}/deactivate
```

See the service README for full API examples:

```txt
services/sports-catalog-service/README.md
```

## Planned Services

Future services may include:

* `football-service`
* `stats-service`
* Additional sport-specific services

## Tech Stack

* Java 21
* Spring Boot
* Maven
* PostgreSQL
* Flyway
* Docker
* Docker Compose
* JUnit
* Mockito
* Testcontainers

## Local Development

To build the current service:

```bash
cd services/sports-catalog-service
./mvnw clean test
```

To run the service locally:

```bash
cd services/sports-catalog-service
./mvnw spring-boot:run
```

Health check:

```bash
curl http://localhost:8080/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

## Contribution Flow

The project follows a branch-based workflow:

```txt
Issue -> Branch from develop -> Pull Request -> Review -> Merge into develop
```

Pull requests should target `develop`.

The `main` branch is reserved for stable releases.

## License

This project is licensed under the MIT License.
