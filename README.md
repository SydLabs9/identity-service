# Identity Service

Generic, extensible identity service foundation built with Java and Spring Boot.

## Why this service exists

This service is designed to centralize identity and authentication concerns while allowing security mechanisms to evolve over time.

The core idea is:

- Keep the identity domain stable (`User`, lifecycle, credentials state).
- Add authentication and protocol mechanisms as pluggable adapters.
- Let consumer services trust contracts (tokens/claims in later phases), not raw credentials.

## Should identity capabilities be bundled together?

Bundling is a good starting model when:

- one team owns identity capabilities end to end,
- release cadence is shared,
- operational simplicity is a priority.

Split into multiple services later when:

- protocols diverge significantly (for example separate OAuth2 Authorization Server needs),
- compliance boundaries require isolation,
- scaling patterns differ enough to justify independent deployment.

Recommended current approach: start bundled with strict module boundaries, then split only with clear operational need.

## Implemented now (Phase 1)

- Spring Boot 3 + Java 21 + Gradle foundation.
- Registration endpoint: `POST /api/auth/register`.
- Login endpoint: `POST /api/auth/login`.
- BCrypt password hashing and verification.
- Validation and API error handling (`400`, `401`, `409`).
- Integration tests for happy and negative auth flows.

## Planned next (not implemented yet)

- `POST /api/auth/refresh`.
- JWT access/refresh issuance and validation contracts.
- JWKS endpoint for consumer service token verification.
- Protected route example (`/api/me`).
- GitHub Actions CI workflow.
- AWS deployment documentation baseline.

## Extensibility model

Current extension seam:

- `Authenticator` interface at `src/main/java/com/example/identity/auth/Authenticator.java`
- `PasswordAuthenticator` implementation at `src/main/java/com/example/identity/auth/PasswordAuthenticator.java`

Contribution path for new auth/security mechanisms:

1. Add a new authenticator/provider (for example MFA challenge authenticator, federated authenticator, passwordless authenticator).
2. Wire request DTOs and routing only as needed, without breaking existing endpoints.
3. Add tests for success and failure paths.
4. Update docs and changelog with clear implemented scope.

Guideline:

- Extend existing module when behavior fits current auth contract.
- Introduce a new module/package when protocol or lifecycle concerns differ substantially.

## API quickstart

### Register

`POST /api/auth/register`

Request:

```json
{
  "email": "user@example.com",
  "password": "StrongPass123!"
}
```

Response (`201 Created`):

```json
{
  "userId": "uuid-value",
  "email": "user@example.com"
}
```

### Login

`POST /api/auth/login`

Request:

```json
{
  "email": "user@example.com",
  "password": "StrongPass123!"
}
```

Response (`200 OK`):

```json
{
  "userId": "uuid-value",
  "email": "user@example.com"
}
```

## Run locally

Prerequisites:

- Java 21

Commands:

- `./gradlew bootRun`
- `./gradlew test`

Default dev data store is in-memory H2 configured in `src/main/resources/application.yml`.

## Current package layout

- `com.example.identity.config` - security configuration (`SecurityFilterChain`, password encoder)
- `com.example.identity.user` - user entity, repository, and domain service
- `com.example.identity.auth` - auth API, DTOs, exceptions, authenticator abstraction
- `com.example.identity.web` - global API exception mapping
