# Contributing

Thanks for contributing to the Identity Service.

This project is intentionally designed to be extensible. Please preserve clear boundaries between:

- identity domain (`user` package),
- authentication mechanisms (`auth` package, `Authenticator` contract),
- transport/protocol adapters (future JWT/OAuth2/OIDC layers).

## Local setup

Prerequisites:

- Java 21

Run:

- `./gradlew bootRun`
- `./gradlew test`

## Development principles

- Keep behavior backward-compatible for existing endpoints.
- Prefer small, focused pull requests.
- Add or update tests for every behavior change.
- Document user-facing or architecture-impacting changes in `README.md` and `CHANGELOG.md`.

## Extending authentication

Current extension seam:

- `Authenticator` interface:
  - `src/main/java/com/example/identity/auth/Authenticator.java`
- Default implementation:
  - `src/main/java/com/example/identity/auth/PasswordAuthenticator.java`

When adding a new auth flavor (for example MFA, social login, passwordless):

1. Implement an authenticator/provider for the new mechanism.
2. Wire controller/service integration without breaking existing register/login flows.
3. Add positive and negative test cases.
4. Update docs:
   - `README.md` implemented vs planned sections
   - `CHANGELOG.md` with shipped behavior only

## API and validation conventions

- Keep request DTO validation strict and explicit.
- Return deterministic HTTP status codes:
  - `400` for invalid input
  - `401` for invalid credentials/auth failures
  - `409` for business conflicts (for example duplicate registration)
- Reuse centralized error handling patterns in `ApiExceptionHandler`.

## Testing expectations

At minimum, cover:

- happy path behavior,
- invalid input handling,
- auth failure paths,
- conflict paths.

Prefer integration tests for endpoint behavior and contract checks.

## Branching, releases, and commits

- **`dev`** — day-to-day integration (feature PRs land here).
- **`main`** — stable; each merge gets a **SemVer tag** and CHANGELOG section (see [docs/RELEASE.md](docs/RELEASE.md)).
- Branch naming: `feature/<short-name>`, `fix/<short-name>`, `chore/<short-name>`
- Commit messages: concise, imperative, scoped (`feat(auth): …`, `docs: …`).

Before merging to **`main`**, move CHANGELOG items from **`[Unreleased]`** into a versioned heading with a one-line **Theme**.

## Pull request checklist

- [ ] Code compiles and tests pass locally.
- [ ] New behavior includes tests.
- [ ] Docs updated (`README.md`, `CHANGELOG.md`, and this file when needed).
- [ ] No unfinished debug code or dead paths.
