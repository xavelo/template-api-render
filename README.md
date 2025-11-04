# Render API Template

This project provides a starting point for building RESTful services with **Spring Boot 3** and **Java 21**. It demonstrates a hexagonal architecture separating HTTP adapters, application services, and persistence adapters.

## Features

* Endpoints for managing users, guardians, students, and authorizations.
* `/api/ping` and `/api/secure/ping` endpoints for liveness and authentication checks.
* PostgreSQL persistence with Flyway database migrations.
* OpenAPI documentation generated with springdoc.
* Dockerfile for containerised builds.

## Requirements

* Java 21
* Maven
* Docker (optional)

## Building and Running

Run the tests:

```bash
./mvnw test
```

The application uses an in-memory H2 database by default.

To run against PostgreSQL, provide connection details via environment variables:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/postgres
export DB_USER=postgres
export DB_PASS=postgres
export DB_DRIVER=org.postgresql.Driver
export FLYWAY_ENABLED=true
./mvnw spring-boot:run
```

The H2 console is enabled at `http://localhost:8080/h2-console`.

Build and run the Docker image:

```bash
docker build -t render-template-api .
docker run -p 8080:8080 render-template-api
```


## API Documentation

After the application is running, navigate to
`http://localhost:8080/swagger-ui/index.html` to view the interactive Swagger
UI and explore the available endpoints.

