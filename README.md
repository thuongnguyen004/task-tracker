# Task Tracker API

Backend service for the Task Tracker application.

## Local Dev Setup

### Requirements

- Java 21
- Maven Wrapper included in this package
- PostgreSQL database

### Configure Environment

The existing development profile is `dev` and reads from `src/main/resources/application-dev.yaml`.

Set the required JWT environment variables before running the app:

```powershell
$env:JWT_SECRET_KEY="replace-with-at-least-64-bytes-secret"
$env:ACCESS_TOKEN_EXPIRATION_TIME="3600000"
$env:REFRESH_TOKEN_EXPIRATION_TIME="604800000"
```

Update these values in `src/main/resources/application-dev.yaml` for your local database and frontend origin:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/task-tracker
    username: your-db-username
    password: your-db-password

app:
  cors:
    allowed-origins:
      - http://localhost:5173
```

### Run Locally

```powershell
.\mvnw spring-boot:run
```

The API uses context path `/api`, so the default local base URL is:

```text
http://localhost:8080/api
```

### Build And Test

```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd test
```

## Branching & Commit Message Conventions

### Branch Naming

Use lowercase words separated by hyphens:

```text
<type>/<short-description>
```

Examples:

```text
feature/ticket-status-update
fix/login-token-expiration
chore/update-dependencies
docs/readme-setup
```

Allowed branch types:

- `feature`
- `fix`
- `chore`
- `docs`
- `refactor`
- `test`

### Commit Message Format

Use Conventional Commit style:

```text
<type>(<scope>): <short summary>
```

Examples:

```text
feat(ticket): add status transition history
fix(auth): validate refresh token ownership
docs(readme): add local development setup
refactor(entity): normalize ticket constraints
```

Common commit types:

- `feat`: new feature
- `fix`: bug fix
- `docs`: documentation only
- `refactor`: code change without behavior change
- `test`: add or update tests
- `chore`: maintenance work
- `build`: build or dependency changes

Keep commits focused and write summaries in the imperative mood.

## Tech Stack & Versions

| Tool / Library | Version |
| --- | --- |
| Java | 21 |
| Spring Boot | 4.1.0 |
| Maven | Maven Wrapper |
| PostgreSQL JDBC Driver | Managed by Spring Boot 4.1.0 |
