[![CI/CD Pipeline](https://github.com/xavelo/api-template-spring-boot/actions/workflows/ci-cd.yaml/badge.svg)](https://github.com/xavelo/api-template-spring-boot/actions/workflows/ci-cd.yaml)

## Configuration

Database connection properties are supplied via environment variables:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME` (optional, defaults to `org.postgresql.Driver`)

