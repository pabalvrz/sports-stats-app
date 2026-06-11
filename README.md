# sports-stats-app

Open source microservices-based sports statistics platform built with Java, Spring Boot, PostgreSQL and Docker.

## Project status

This project is in its initial setup phase.

The current goal is to establish a clean monorepo structure, define the first backend microservice and prepare the project for future collaborative development.

## Architecture approach

This repository is organized as a monorepo.

Each backend service will live inside the `services/` directory as an independent Spring Boot application.

```txt
sports-stats-app/
├── services/
│   └── sports-catalog-service/
├── infrastructure/
├── docs/
├── .github/
├── README.md
├── CONTRIBUTING.md
└── LICENSE
```

## Current services

### sports-catalog-service

Initial Spring Boot microservice responsible for the shared sports catalog domain.

This service will be the base for common sports data such as:

* Sports
* Countries
* Competitions
* Seasons
* Base catalog entities shared by future sport-specific services

Current status:

* Spring Boot project created
* Java 21 configured
* Maven configured
* Docker Compose support enabled
* PostgreSQL local dependency prepared
* Actuator health check available

## Planned services

Future services may include:

* `football-service`
* `stats-service`
* Additional sport-specific services

## Tech stack

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

## Local development

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

## Contribution flow

The project follows a branch-based workflow:

```txt
Issue → Branch from develop → Pull Request → Review → Merge into develop
```

Pull requests should target `develop`.

The `main` branch is reserved for stable releases.

## License

This project is licensed under the MIT License.
