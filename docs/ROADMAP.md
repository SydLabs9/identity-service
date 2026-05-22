# Roadmap

Generic **authentication** and **authorization** platform for Java/Spring consumers. Shipped work is tagged on **`main`**; this page tracks GitHub Issues and the org project board.

**Product:** [Vision](../product/VISION.md) · [Scope](../product/SCOPE.md)

**Project board:** [SydLabs9 — Project 1](https://github.com/orgs/SydLabs9/projects/1/views/1) · [Issues](https://github.com/SydLabs9/identity-service/issues)

## Vision (levels)

| Level | Focus | Status |
|-------|--------|--------|
| **0 — Current** | Password auth, Postgres identity store, API docs | [v0.1.0](https://github.com/SydLabs9/identity-service/releases/tag/v0.1.0) → [v0.3.0](https://github.com/SydLabs9/identity-service/releases/tag/v0.3.0) |
| **1 — Tokens** | JWT access/refresh, JWKS, protected `/api/me` | [Milestone v0.4.0](https://github.com/SydLabs9/identity-service/milestone/1) |
| **2 — Authz data** | Roles, OAuth clients, policy hooks | Planned (issues #9+) |
| **3 — Protocols** | OAuth2/OIDC gateway, federated IdP | Future epic ([#21](https://github.com/SydLabs9/identity-service/issues/21)) |

Diagram: [architecture-future-platform.png](./architecture-future-platform.png) · Current: [architecture-level-0-current.png](./architecture-level-0-current.png)

## Shipped (closed issues)

| Release | Theme | Issue |
|---------|--------|-------|
| [v0.1.0](https://github.com/SydLabs9/identity-service/releases/tag/v0.1.0) | Auth foundation | [#12](https://github.com/SydLabs9/identity-service/issues/12) |
| [v0.2.0](https://github.com/SydLabs9/identity-service/releases/tag/v0.2.0) | Database-backed identity | [#13](https://github.com/SydLabs9/identity-service/issues/13) |
| [v0.3.0](https://github.com/SydLabs9/identity-service/releases/tag/v0.3.0) | Published OpenAPI / Redoc | [#14](https://github.com/SydLabs9/identity-service/issues/14) |

## Next up (open issues)

| Priority | Work | Issue |
|----------|------|-------|
| P0 | Enable GitHub Pages for API docs | [#22](https://github.com/SydLabs9/identity-service/issues/22) |
| P0 | CI — `./gradlew test` on PR | [#19](https://github.com/SydLabs9/identity-service/issues/19) |
| P1 | JWT access token issuance | [#15](https://github.com/SydLabs9/identity-service/issues/15) |
| P1 | Refresh token endpoint | [#16](https://github.com/SydLabs9/identity-service/issues/16) |
| P1 | JWKS endpoint | [#17](https://github.com/SydLabs9/identity-service/issues/17) |
| P1 | Protected `GET /api/me` | [#18](https://github.com/SydLabs9/identity-service/issues/18) |
| P2 | Authz foundation (roles, clients schema) | [#20](https://github.com/SydLabs9/identity-service/issues/20) |
| P3 | OAuth2/OIDC gateway (epic) | [#21](https://github.com/SydLabs9/identity-service/issues/21) |

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
gh project item-add 1 --owner SydLabs9 --url https://github.com/SydLabs9/identity-service/issues/15
```

Until then, open the [project board](https://github.com/orgs/SydLabs9/projects/1/views/1) → **Add item** → link each issue above (Done column for #12–#14, Backlog/Ready for #15–#22).
