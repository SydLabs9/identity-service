# AGENTS.md

## Cursor Cloud specific instructions

### Architecture

Single Spring Boot 3.3 + Java 21 application backed by PostgreSQL 16. See `README.md` for full details.

### Running the service

1. **Start PostgreSQL** (required for `dev` profile):
   ```
   sudo pg_ctlcluster 16 main start
   ```
   The database `identity_service` with user `identity_app`/`identity_app` must exist. Create them if missing:
   ```
   sudo -u postgres psql -c "CREATE USER identity_app WITH PASSWORD 'identity_app';"
   sudo -u postgres psql -c "CREATE DATABASE identity_service OWNER identity_app;"
   ```

2. **Start the app**: `./gradlew bootRun --no-daemon` — runs on port 8080 with `dev` profile by default. Liquibase migrations run automatically on startup.

### Testing

- `./gradlew test` — runs unit + integration tests using H2 (no PostgreSQL or Docker needed).
- No separate lint tool is configured; `./gradlew compileJava` serves as the compilation/lint check.
- Testcontainers-based Postgres integration test (`AuthControllerPostgresIntegrationTest`) requires Docker; it is skipped automatically when Docker is unavailable.

### Key endpoints (when app is running)

- `POST /api/auth/register` — register a user
- `POST /api/auth/login` — authenticate
- `GET /v3/api-docs` — OpenAPI JSON spec
- `GET /swagger-ui/index.html` — Swagger UI

### Gotchas

- The `dev` profile expects PostgreSQL on `localhost:5432` with database `identity_service`. If Postgres is not running, `bootRun` will fail at startup with a connection refused error.
- Spring Security auto-generates a password on startup (visible in logs). This does not affect the `/api/auth/*` endpoints, which use custom authentication.
- `./gradlew openapi` is a separate Gradle task (tag-filtered) that exports `build/openapi/openapi.json`; it is excluded from the default `test` task.

### GitHub org access (SydLabs9)

The Cursor secrets system may fail to sync fine-grained PATs (400 error). Use the **device flow** workaround instead:

```bash
mkdir -p /tmp/gh-org
GH_CONFIG_DIR=/tmp/gh-org gh auth login --hostname github.com --git-protocol https --scopes repo,admin:org,project --web
```

This prompts a one-time code — visit https://github.com/login/device and enter it. Then use org commands with:

```bash
export GH_CONFIG_DIR=/tmp/gh-org
gh issue list -R SydLabs9/identity-service
gh api /repos/SydLabs9/identity-service ...
```

The personal fork remote is `origin` (`sydlab/identity-service`); the org remote is `upstream` (`SydLabs9/identity-service`).

### PM/Scrum agent workflow

This repo uses an issue-driven development workflow tracked on the [SydLabs9 project board](https://github.com/orgs/SydLabs9/projects/1/views/1).

**Branching model:**
- `main` (stable, tagged releases) ← `dev` (integration) ← `feature/*` or `fix/*` (working branches)
- Development happens on the personal fork (`sydlab/identity-service`), PRs target `SydLabs9/identity-service:dev`
- Release merges go `dev` → `main` on the org repo

**Project board columns:** Backlog → Ready → In progress → In review → Done

**Issue lifecycle:**
1. Issues are created on `SydLabs9/identity-service` and added to the project board
2. Work starts on a feature branch in the personal fork → move issue to "In progress"
3. PR opened to org's `dev` branch → move issue to "In review"
4. PR merged + DoD met → move issue to "Done"

**Definition of Done (DoD):**
- Linked PR merged to `dev` (or `main` for releases)
- `./gradlew test` passes (CI on GitHub Actions)
- `CHANGELOG.md` updated under `[Unreleased]`
- Code review completed

**Labels:** `auth`, `authz`, `infrastructure`, `documentation`, `shipped`, `roadmap`

**Slack integration:** The Slack MCP server must be authenticated to post to `#sydlab-scrum`. If unavailable, skip Slack updates.
