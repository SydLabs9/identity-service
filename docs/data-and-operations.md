# Data architecture and operations

## Logical model

| Concept | Notes |
|---------|-------|
| `User` | JPA entity mapped to table `users` |
| Identifier | UUID primary key (`id`) |
| Email | Unique, normalized to lowercase at registration |
| Credentials | BCrypt hash in `password_hash` |
| State | Boolean `enabled` (defaults true in schema and entity) |

## Physical schema

Schema is owned by **Liquibase**, not Hibernate DDL:

- Changelog master: [`database/liquibase/db.changelog-master.yaml`](../database/liquibase/db.changelog-master.yaml)
- Changesets live under [`database/liquibase/changes/`](../database/liquibase/changes/)

Spring configuration uses a **filesystem** changelog location (`file:database/liquibase/...`). That means:

- Running from the repo root: the `database/` directory must exist on the working directory.
- **Docker:** the runtime image copies `database/` beside the JAR (see repo `Dockerfile`).

JPA **`ddl-auto: validate`** ensures the entity model matches Liquibase-produced tables.

## Environments

| Profile | Database | Typical use |
|---------|----------|-------------|
| `dev` (default in `application.yml`) | PostgreSQL via `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | Local or Compose |
| `prod` | PostgreSQL/Aurora; JDBC URL/credentials required | Hosted environments |
| `test` | H2 in-memory, PostgreSQL compatibility mode | Default `./gradlew test` (fast) |
| Testcontainers (profile `dev` + JDBC override) | Ephemeral Postgres 16 | `AuthControllerPostgresIntegrationTest`; skipped when Docker unavailable |

Operational SQL for granting least-privilege app users (e.g. Aurora) lives under [`database/aurora/`](../database/aurora/).

## Docker Compose

`docker-compose.yml` starts PostgreSQL with user/database aligned to `dev` defaults, then the application with `DB_URL` pointing at the `postgres` service hostname. Liquibase runs on startup against that database.

For a deeper schema narrative and ER-style notes, see [`database/README.md`](../database/README.md).
