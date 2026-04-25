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

### Planned (not shipped yet)

- Refresh endpoint (`POST /api/auth/refresh`).
- JWT token issuance and JWKS endpoint.
- Protected route example (`/api/me`).
- GitHub Actions CI workflow and AWS deployment guidance docs.
