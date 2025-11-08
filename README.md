# Filocitas API

Filocitas API is a Spring Boot 3 reference implementation that demonstrates how to structure a production-ready REST service using a hexagonal (ports & adapters) architecture. The project targets Java 21 and comes pre-configured with API contracts generated from OpenAPI, database migrations, observability defaults, and Docker tooling so you can focus on delivering business features quickly.

## Architecture at a Glance

* **Modules**
  * `api` &mdash; hosts the OpenAPI specification and generates type-safe interfaces and DTOs that are shared across adapters.
  * `application` &mdash; contains the Spring Boot application, domain services, persistence adapters, and configuration.
* **Hexagonal boundaries**
  * `port/in` and `port/out` packages define the application-facing contracts.
  * `adapter/in` contains REST controllers and other inbound adapters.
  * `adapter/out` contains persistence, messaging, and integration adapters.
* **Support libraries**
  * MapStruct for DTO/entity mapping.
  * Spring Security for authentication/authorization.
  * Spring Data JPA + Flyway for persistence and schema migrations.
  * Caffeine for caching and Spring Scheduling for background jobs.

## Prerequisites

* Java 21 (Temurin or Oracle distributions are both supported)
* Maven 3.9+
* Docker (optional, for container builds)
* PostgreSQL 14+ (optional, H2 is used by default for local development)

## Getting Started

Clone the repository and bootstrap the application:

```bash
./mvnw spring-boot:run -pl application -am
```

The command compiles OpenAPI interfaces from the `api` module, builds the application module, and starts the service on <http://localhost:8080>.

### Running the Test Suite

```bash
./mvnw test
```

All unit and integration tests live inside the `application` module. The suite runs against an in-memory H2 database and applies Flyway migrations on startup.

### Common Developer Tasks

| Task | Command |
| ---- | ------- |
| Re-generate OpenAPI interfaces | `./mvnw generate-sources -pl api` |
| Build an executable jar | `./mvnw clean package -pl application` |
| Run only application module tests | `./mvnw test -pl application` |

## Configuration

Runtime properties are managed via `application.yaml`. Key environment variables include:

| Variable | Default | Description |
| -------- | ------- | ----------- |
| `DB_URL` | `jdbc:h2:mem:appdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL` | JDBC connection string. Override for PostgreSQL. |
| `DB_USER` / `DB_PASS` | `sa` / *(empty)* | Database credentials. |
| `DB_DRIVER` | `org.h2.Driver` | Fully-qualified JDBC driver class. |
| `DB_POOL_SIZE` | `5` | Maximum HikariCP pool size. |
| `FLYWAY_ENABLED` | `true` | Enables schema migrations on startup. |
| `ADMIN_USERNAME` / `ADMIN_PASSWORD` | `admin` / `changeme` | Credentials for administrative endpoints. |

When connecting to PostgreSQL, set the corresponding variables before launching the service:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/filocitas
export DB_USER=postgres
export DB_PASS=postgres
export DB_DRIVER=org.postgresql.Driver
export FLYWAY_ENABLED=true
./mvnw spring-boot:run -pl application -am
```

## Database Migrations

Flyway migration scripts live in `application/src/main/resources/db/migration`. Migrations execute automatically on application startup. To run them manually, execute:

```bash
./mvnw flyway:migrate -pl application
```

## API Documentation

Once the service is running, navigate to the automatically generated Swagger UI:

* <http://localhost:8080/swagger-ui/index.html>

The OpenAPI descriptor is also available at <http://localhost:8080/v3/api-docs>. These definitions are produced by the SpringDoc integration and kept in sync with the `api` module sources.

## Project Structure

```
.
├── api
│   ├── src/main/resources/openapi/filocitas-api.yaml   # OpenAPI 3 specification
│   └── target/generated-sources                         # Generated interfaces (after build)
├── application
│   ├── src/main/java/com/xavelo/filocitas              # Domain, ports, adapters, config
│   ├── src/main/resources                              # Spring Boot configuration & data
│   └── src/test/java                                   # Tests
├── requests                                            # Postman/REST client examples
├── Dockerfile
└── README.md
```

## Docker Workflow

Build and run the container image locally:

```bash
docker build -t filocitas-api .
docker run --rm -p 8080:8080 filocitas-api
```

The container image bundles the Spring Boot fat jar built from the `application` module. Override environment variables using `-e` flags when running the container.

## Observability & Ops

* **Health checks** &mdash; `/api/admin/ping` (public liveness) and `/api/secure/ping` (authentication-gated) endpoints verify the service status.
* **H2 console** &mdash; available at <http://localhost:8080/h2-console> when using the default profile.
* **Startup diagnostics** &mdash; a `DataSourceDiagnosticsApplicationListener` logs connection pool information at boot to simplify troubleshooting.

## Contributing

1. Create a feature branch from `main`.
2. Make your changes and ensure `./mvnw test` passes.
3. Submit a pull request describing your changes and any additional configuration required.

## License

This template is provided without a specific license. Add your organization's preferred license or usage terms before distributing.
