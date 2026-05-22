# Roadmap

Generic **authentication** and **authorization** platform for Java/Spring consumers. Shipped work is tagged on **`main`**; this page tracks GitHub Issues and the org project board.

**Project board:** [SydLabs9 — Project 1](https://github.com/orgs/SydLabs9/projects/1/views/1) (add/link issues from [sydlab/identity-service](https://github.com/sydlab/identity-service/issues)).

## Vision (levels)

| Level | Focus | Status |
|-------|--------|--------|
| **0 — Current** | Password auth, Postgres identity store, API docs | [v0.1.0](https://github.com/sydlab/identity-service/releases/tag/v0.1.0) → [v0.3.0](https://github.com/sydlab/identity-service/releases/tag/v0.3.0) |
| **1 — Tokens** | JWT access/refresh, JWKS, protected `/api/me` | [Milestone v0.4.0](https://github.com/sydlab/identity-service/milestone/1) |
| **2 — Authz data** | Roles, OAuth clients, policy hooks | Planned (issues #9+) |
| **3 — Protocols** | OAuth2/OIDC gateway, federated IdP | Future epic ([#10](https://github.com/sydlab/identity-service/issues/10)) |

Diagram: [architecture-future-platform.png](./architecture-future-platform.png) · Current: [architecture-level-0-current.png](./architecture-level-0-current.png)

## Shipped (closed issues)

| Release | Theme | Issue |
|---------|--------|-------|
| [v0.1.0](https://github.com/sydlab/identity-service/releases/tag/v0.1.0) | Auth foundation | [#1](https://github.com/sydlab/identity-service/issues/1) |
| [v0.2.0](https://github.com/sydlab/identity-service/releases/tag/v0.2.0) | Database-backed identity | [#2](https://github.com/sydlab/identity-service/issues/2) |
| [v0.3.0](https://github.com/sydlab/identity-service/releases/tag/v0.3.0) | Published OpenAPI / Redoc | [#3](https://github.com/sydlab/identity-service/issues/3) |

## Next up (open issues)

| Priority | Work | Issue |
|----------|------|-------|
| P0 | Enable GitHub Pages for API docs | [#11](https://github.com/sydlab/identity-service/issues/11) |
| P0 | CI — `./gradlew test` on PR | [#8](https://github.com/sydlab/identity-service/issues/8) |
| P1 | JWT access token issuance | [#4](https://github.com/sydlab/identity-service/issues/4) |
| P1 | Refresh token endpoint | [#5](https://github.com/sydlab/identity-service/issues/5) |
| P1 | JWKS endpoint | [#6](https://github.com/sydlab/identity-service/issues/6) |
| P1 | Protected `GET /api/me` | [#7](https://github.com/sydlab/identity-service/issues/7) |
| P2 | Authz foundation (roles, clients schema) | [#9](https://github.com/sydlab/identity-service/issues/9) |
| P3 | OAuth2/OIDC gateway (epic) | [#10](https://github.com/sydlab/identity-service/issues/10) |

Target release for P1 cluster: **v0.4.0** — see [CHANGELOG](../CHANGELOG.md) `[Unreleased]` when work starts.

## Labels

| Label | Use |
|-------|-----|
| `auth` | Authentication (credentials, tokens) |
| `authz` | Authorization (roles, clients, policies) |
| `infrastructure` | CI, Docker, Pages, ops |
| `documentation` | OpenAPI, architecture docs |
| `shipped` | Delivered in a tagged release |
| `roadmap` | Planned, not yet on `main` |

## Sync issues to the org project board

The CLI token needs **`project`** scope to add items automatically:

```bash
gh auth refresh -s read:project,project
gh project item-add 1 --owner SydLabs9 --url https://github.com/sydlab/identity-service/issues/4
```

Until then, open the [project board](https://github.com/orgs/SydLabs9/projects/1/views/1) → **Add item** → link each issue above (Done column for #1–#3, Backlog/Ready for #4–#11).
