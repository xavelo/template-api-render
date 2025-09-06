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

Start the application with a PostgreSQL database by providing connection details via environment variables:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/postgres
export DB_USER=postgres
export DB_PASS=postgres
./mvnw spring-boot:run
```

For local development without PostgreSQL, an in-memory H2 profile is available:

```bash
SPRING_PROFILES_ACTIVE=h2 ./mvnw spring-boot:run
```

The H2 console is enabled at `http://localhost:8080/h2-console` when using this profile.

Build and run the Docker image:

```bash
docker build -t render-template-api .
docker run -p 8080:8080 render-template-api
```


## API Documentation

After the application is running, navigate to
`http://localhost:8080/swagger-ui/index.html` to view the interactive Swagger
UI and explore the available endpoints.

