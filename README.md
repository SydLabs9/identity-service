# Identity Service

Generic, extensible **authentication and authorization** service foundation built with Java and Spring Boot.

**Tracking:** [Vision & scope](product/README.md) · [Roadmap](docs/ROADMAP.md) · [GitHub Issues](https://github.com/SydLabs9/identity-service/issues) · [SydLabs9 project board](https://github.com/orgs/SydLabs9/projects/1/views/1)

**Product:** [product/](product/README.md) (vision, scope) · **Architecture:** [docs/](docs/README.md) (overview, application design, data and ops).

**Releases:** stable tags on **`main`** — see [CHANGELOG.md](CHANGELOG.md) and [docs/RELEASE.md](docs/RELEASE.md).

| Version | Theme |
|---------|--------|
| [v0.3.0](https://github.com/SydLabs9/identity-service/releases/tag/v0.3.0) | Published OpenAPI / Redoc (GitHub Pages) |
| [v0.2.0](https://github.com/SydLabs9/identity-service/releases/tag/v0.2.0) | Database-backed identity (Postgres, Liquibase, Docker) |
| [v0.1.0](https://github.com/SydLabs9/identity-service/releases/tag/v0.1.0) | Auth foundation (register / login) |

Development branch: **`dev`**. Default integration PRs target **`dev`**; release PRs merge **`dev` → `main`**.

## Why this service exists

This service is designed to centralize identity and authentication concerns while allowing security mechanisms to evolve over time.

The core idea is:

- Keep the identity domain stable (`User`, lifecycle, credentials state).
- Add authentication and protocol mechanisms as pluggable adapters.
- Let consumer services trust contracts (tokens/claims in later phases), not raw credentials.
- Bundling versus splitting deployments: see [docs/overview.md](docs/overview.md).

## Implemented now (Phase 1 + Phase 2A)

- Spring Boot 3 + Java 21 + Gradle foundation.
- Registration endpoint: `POST /api/auth/register`.
- Login endpoint: `POST /api/auth/login`.
- BCrypt password hashing and verification.
- Validation and API error handling (`400`, `401`, `409`).
- Integration tests for happy and negative auth flows.
- Unit tests for `UserService` (register/authenticate branches).
- Docker image and Compose stack to run the app with PostgreSQL (see **Docker** under Run locally).
- Liquibase-managed schema migrations.
- PostgreSQL-first datasource configuration via Spring profiles (`dev`, `prod`).
- PostgreSQL-backed smoke tests with Testcontainers (`AuthControllerPostgresIntegrationTest`) when Docker is available.
- Aurora PostgreSQL grants/runbook assets under `database/aurora/`.
- SpringDoc OpenAPI (`/v3/api-docs`, Swagger UI except in `prod`) and **`./gradlew openapi`** to emit `build/openapi/openapi.json`.
- Redocly + GitHub Actions workflow publishing static API docs to GitHub Pages (see **OpenAPI and API documentation** below).

## Planned next (not implemented yet)

Tracked as GitHub Issues — full table in [docs/ROADMAP.md](docs/ROADMAP.md).

- **v0.4.0 milestone:** JWT access/refresh, JWKS, protected `/api/me` ([#4](https://github.com/SydLabs9/identity-service/issues/4)–[#7](https://github.com/SydLabs9/identity-service/issues/7))
- **Infra:** CI on PR ([#8](https://github.com/SydLabs9/identity-service/issues/8)), enable Pages ([#11](https://github.com/SydLabs9/identity-service/issues/11))
- **Later authz / protocols:** roles & clients ([#9](https://github.com/SydLabs9/identity-service/issues/9)), OAuth2/OIDC epic ([#10](https://github.com/SydLabs9/identity-service/issues/10))

## Extensibility model

Plug in credential verification via **`Authenticator`** / **`PasswordAuthenticator`**. Contribution flow and layering: [docs/application-architecture.md](docs/application-architecture.md).

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

## OpenAPI and API documentation

- **JSON spec** (runtime): `GET /v3/api-docs` (while the app is running).
- **Export for tooling / CI**: `./gradlew openapi` writes `build/openapi/openapi.json`.
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html` when not using the `prod` profile (`springdoc.swagger-ui.enabled` is `false` in [`application-prod.yml`](src/main/resources/application-prod.yml)).
- **Published Redoc (GitHub Pages)**: workflow [`.github/workflows/openapi-pages.yml`](.github/workflows/openapi-pages.yml) runs on pushes to `dev` and `main`. In the repo **Settings → Pages**, set **Build and deployment** source to **GitHub Actions**. The site URL is usually `https://<owner>.github.io/<repo>/` (for example `https://sydlab.github.io/identity-service/`).

## Run locally

Prerequisites:

- Java 21
- PostgreSQL 14+ (for running the app locally with profile `dev`, or rely on Compose below)

Commands:

- `./gradlew bootRun`
- `./gradlew test` — primary suite uses profile `test` (H2). Optional [`AuthControllerPostgresIntegrationTest`](src/test/java/com/example/identity/auth/AuthControllerPostgresIntegrationTest.java) exercises PostgreSQL + Liquibase via **Testcontainers** when Docker is available; skipped otherwise.

### Docker (app + PostgreSQL)

Prerequisites:

- Docker with Compose v2

From the repo root:

- `docker compose up --build`

This builds the [`Dockerfile`](Dockerfile) image, starts [`postgres`](docker-compose.yml) with a healthcheck, then starts the identity service once the database is ready. The app listens on host port **8080** and uses profile `dev` with:

- `DB_URL=jdbc:postgresql://postgres:5432/identity_service` (host `postgres` is the Compose service name)
- `DB_USERNAME` / `DB_PASSWORD`: `identity_app` (matching the Postgres container defaults)

Liquibase changelog files are copied into the image under `database/` so `file:database/liquibase/...` resolves at runtime.

With profile `dev` and no Compose overrides (local Postgres or similar), JDBC defaults are:

- `DB_URL` — default `jdbc:postgresql://localhost:5432/identity_service`
- `DB_USERNAME` — default `identity_app`
- `DB_PASSWORD` — default `identity_app`

## Database migrations and profiles

- Liquibase changelog master:
  - `database/liquibase/db.changelog-master.yaml`
- Initial changesets:
  - `database/liquibase/changes/001-create-users-table.yaml`
  - `database/liquibase/changes/002-add-users-indexes.yaml`

Profile configuration:

- `application-dev.yml`: local PostgreSQL
- `application-prod.yml`: Aurora/PostgreSQL connection via environment variables
- `application-test.yml`: H2 for the default `./gradlew test` suite
- Postgres + Liquibase: also covered by [`AuthControllerPostgresIntegrationTest`](src/test/java/com/example/identity/auth/AuthControllerPostgresIntegrationTest.java) when Docker is running

JPA is configured with `ddl-auto=validate`; schema evolution is managed by Liquibase.

## Aurora PostgreSQL grants

Aurora SQL assets are included for DBA/app-user provisioning:

- `database/aurora/grants.sql`
- `database/aurora/README.md`
- `database/README.md` (schema summary, ER diagram, and extension model)

Use these to create app role/user permissions and wire production env vars for the `prod` profile.
