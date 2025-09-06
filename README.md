# Render API Template

This project provides a starting point for building RESTful services with **Spring Boot 3** and **Java 17**. It demonstrates a hexagonal architecture separating HTTP adapters, application services, and persistence adapters.

## Features

* Endpoints for managing users, guardians, students, and authorizations.
* `/api/ping` and `/api/secure/ping` endpoints for liveness and authentication checks.
* PostgreSQL persistence with Flyway database migrations.
* OpenAPI documentation generated with springdoc.
* Dockerfile for containerised builds.

## Requirements

* Java 17
* Maven
* Docker (optional)

## Building and Running

Run the tests:

```bash
./mvnw test
```

Start the application:

```bash
./mvnw spring-boot:run
```

Build and run the Docker image:

```bash
docker build -t render-template-api .
docker run -p 8080:8080 render-template-api
```

When running locally, the API documentation is available at `http://localhost:8080/swagger-ui/index.html`.

