# Changelog

All notable changes to this project are documented in this file.

Format: [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) · Versioning: [SemVer](https://semver.org/)

## [Unreleased]

Nothing yet.

## [0.3.0] - 2026-05-05

**Theme: published API contract** — OpenAPI spec generation and static Redoc docs on GitHub Pages.

### Added

- SpringDoc OpenAPI (`/v3/api-docs`, Swagger UI; UI off in `prod`).
- Gradle task `./gradlew openapi` → `build/openapi/openapi.json`.
- Redocly lint/build and GitHub Actions workflow to deploy API docs to Pages.
- OpenAPI annotations on auth endpoints and DTOs.

## [0.2.0] - 2026-05-02

**Theme: database-backed identity** — users stored in PostgreSQL with versioned schema and a runnable local stack.

### Added

- Liquibase migrations for `users` (UUID id, email, password hash, enabled).
- Spring profiles for PostgreSQL (`dev`, `prod`) and H2 tests (`test`).
- Docker image, Compose stack (app + Postgres), and Aurora grants runbook assets.
- Architecture docs (`docs/`) and expanded tests (unit, integration, Testcontainers Postgres).

## [0.1.0] - 2026-05-02

**Theme: auth foundation** — password register/login behind a pluggable `Authenticator` seam.

### Added

- Spring Boot 3 / Java 21 service with `POST /api/auth/register` and `POST /api/auth/login`.
- BCrypt hashing, request validation, and API errors (`400`, `401`, `409`).
- Integration tests for register/login happy path and primary failure modes.
