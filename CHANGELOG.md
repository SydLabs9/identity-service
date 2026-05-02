# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

### Added

- Bootstrapped Spring Boot 3 project using Gradle and Java 21 toolchain.
- Added core user identity model and persistence (`User`, `UserRepository`, `UserService`).
- Added password hashing and verification using BCrypt.
- Added authentication extension seam with `Authenticator` and initial `PasswordAuthenticator`.
- Added auth APIs:
  - `POST /api/auth/register`
  - `POST /api/auth/login`
- Added input validation for register/login DTOs.
- Added global API exception handling for validation and auth errors (`400`, `401`, `409`).
- Added integration tests for:
  - register + login happy path
  - duplicate email conflict
  - invalid password unauthorized response
  - invalid payload bad request response
- Added Liquibase migration framework and changelog structure for schema management.
- Added PostgreSQL runtime driver and profile-based datasource configuration (`dev`, `prod`, `test`).
- Added initial DB migrations for `users` table and email index.
- Added Aurora PostgreSQL grants script and operations runbook:
  - `database/aurora/grants.sql`
  - `database/aurora/README.md`
- Docker (`Dockerfile`, `.dockerignore`, `docker-compose.yml`) to build and run the app with PostgreSQL and health-checked startup.
- Architecture documentation under `docs/`, including Level 0 and future-platform diagrams.
- `UserServiceTest` unit tests for register/authenticate branches.
- Additional auth integration cases (unknown user login, invalid login payload).
- PostgreSQL-backed smoke tests via Testcontainers (`AuthControllerPostgresIntegrationTest`), skipping when Docker is unavailable.

### Planned (not shipped yet)

- Refresh endpoint (`POST /api/auth/refresh`).
- JWT token issuance and JWKS endpoint.
- Protected route example (`/api/me`).
- GitHub Actions CI workflow and AWS deployment guidance docs.
