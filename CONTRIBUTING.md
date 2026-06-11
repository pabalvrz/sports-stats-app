# Contributing Guide

Thank you for your interest in contributing to Open Sports Stats.

Open Sports Stats is an open source microservices-based sports statistics platform built with Java, Spring Boot, PostgreSQL and Docker.

The project starts with football as the first supported sport, but it is designed to grow progressively with new sports, contributors and independent data modules.

## How to contribute

You can contribute in different ways:

* Reporting bugs
* Suggesting new features
* Improving documentation
* Adding tests
* Improving the backend services
* Helping with Docker and local setup
* Proposing new sports modules in the future

## Contribution workflow

All changes must follow this workflow:

1. Open or pick an existing issue.
2. Create a branch from `develop`.
3. Make your changes.
4. Commit your changes with a clear message.
5. Push your branch.
6. Open a pull request targeting `develop`.
7. Wait for review and feedback.
8. Merge only when the pull request is approved and ready.

Pull requests should not target `main` directly.

## Default branch

The default development branch is:

```text
develop
```

All feature branches, documentation branches and task branches must be created from `develop`.

The `main` branch is reserved for stable releases.

## Issue types

Use the appropriate issue type when creating new issues:

* `[Feature]` for new product functionality
* `[Bug]` for bugs or unexpected behavior
* `[Docs]` for documentation changes
* `[Task]` for technical or project tasks

## Branch naming

Use clear and consistent branch names.

Recommended prefixes:

```text
feature/
bugfix/
docs/
task/
test/
refactor/
chore/
```

Examples:

```text
docs/3-add-contributing-guide
feature/create-sports-catalog-service
bugfix/fix-invalid-season-validation
task/add-docker-compose
test/add-country-service-tests
```

## Pull requests

Every pull request should:

* Target `develop`
* Link the related issue
* Explain the changes clearly
* Keep the scope focused
* Include tests when needed
* Update documentation when needed
* Follow the existing project structure and conventions

Use the pull request template provided in the repository.

## Commit messages

Use short and descriptive commit messages.

Examples:

```text
docs: add contributing guide
feature: create sports catalog service
bugfix: fix country iso validation
test: add season service tests
chore: add docker compose base
```

## Local setup

The local setup guide will be added once the initial project structure and Docker configuration are created.

## Architecture

The project is based on microservices.

Each service should follow a clean architecture approach, keeping business logic separated from infrastructure concerns.

More detailed architecture documentation will be added in the project documentation.

## Code of conduct

A code of conduct will be added to define expected behavior for contributors and maintainers.

## Questions

If something is unclear, open an issue or start a discussion before implementing a large change.
