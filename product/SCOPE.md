# Scope

What the Identity Service **will** and **will not** deliver, by phase. Implementation detail lives in [`docs/`](../docs/README.md); release mechanics in [`docs/RELEASE.md`](../docs/RELEASE.md).

**Canonical repo:** [SydLabs9/identity-service](https://github.com/SydLabs9/identity-service)  
**Project board:** [SydLabs9 — Project 1](https://github.com/orgs/SydLabs9/projects/1/views/1)

---

## Level 0 — Foundation (shipped: v0.1.0 → v0.3.0)

### In scope (done)

| Area | Delivered |
|------|-----------|
| **AuthN (password)** | Register, login, BCrypt verification via `Authenticator` seam |
| **Identity store** | Postgres + Liquibase `users` table; H2 for tests |
| **Ops** | Docker Compose stack, `dev` / `prod` / `test` profiles |
| **Contract** | OpenAPI export, Redocly lint, GitHub Pages workflow (enable Pages: [#22](https://github.com/SydLabs9/identity-service/issues/22)) |
| **Quality** | Integration tests (H2 + Testcontainers Postgres), unit tests on services |
| **Process** | SemVer tags on **`main`**, CHANGELOG themes, roadmap + issues |

### Acceptance criteria (met)

- [x] `POST /api/auth/register` and `POST /api/auth/login` with validation and consistent error codes
- [x] Duplicate registration returns **409**; bad credentials return **401**
- [x] Schema migrates cleanly via Liquibase on empty Postgres
- [x] `./gradlew test` passes without Docker; Postgres integration skips gracefully without Docker
- [x] OpenAPI artifact generated and publishable to Pages

### Out of scope for Level 0 (intentionally deferred)

- JWT, refresh tokens, JWKS
- Role-based or attribute-based authorization
- OAuth2/OIDC/SAML endpoints
- Multi-tenant domains, social login, MFA

---

## Level 1 — Tokens (target: v0.4.0)

Issues: [#15](https://github.com/SydLabs9/identity-service/issues/15)–[#18](https://github.com/SydLabs9/identity-service/issues/18) · Milestone: [v0.4.0](https://github.com/SydLabs9/identity-service/milestone/1)

### In scope

| Feature | Description |
|---------|-------------|
| **Access JWT** | Short-lived signed token returned on successful login (and optionally register) |
| **Refresh token** | Rotating or opaque refresh flow with revocation hook (minimal v1: store + validate) |
| **JWKS** | Public keys at `/.well-known/jwks.json` (or documented path) for resource servers |
| **`GET /api/me`** | Bearer-protected endpoint returning current user claims |
| **Security filter** | Spring Security resource-server or custom filter validating JWT signature + expiry |
| **Docs & tests** | OpenAPI updated; integration tests for happy path and 401 cases |

### Acceptance criteria (release gate for v0.4.0)

- [ ] Login returns access token (and refresh token per design doc in PR)
- [ ] Resource server can validate JWT using JWKS **without** shared secret in every service
- [ ] Expired or tampered JWT → **401** on `/api/me`
- [ ] Refresh endpoint issues new access token; invalid refresh → **401**
- [ ] CHANGELOG **Theme** line + GitHub Release notes; issues #15–#18 closed
- [ ] CI runs `./gradlew test` on PR ([#19](https://github.com/SydLabs9/identity-service/issues/19))

### Out of scope for v0.4.0

- Full OAuth2 authorization server (authorize/token with client credentials for third-party apps)
- Fine-grained scopes beyond `openid`-style subject + basic claims
- Distributed session store / SSO cookies

---

## Level 2 — Authorization data (planned: v0.5.x–v0.9.x)

Epic: [#20](https://github.com/SydLabs9/identity-service/issues/20)

### In scope (directional)

| Feature | Description |
|---------|-------------|
| **Roles** | Role entities assigned to users; roles exposed as JWT claims |
| **OAuth clients** | Registered clients (id, secret hash, redirect URIs) for future protocol layer |
| **Policy hooks** | Extension points for “can user X do Y on resource Z” without hard-coding in controllers |
| **Admin APIs** | CRUD for roles/clients (protected; exact shape TBD in issue) |

### Acceptance criteria (draft — refine in #20)

- [ ] User can hold one or more roles persisted in Postgres
- [ ] JWT includes role claims verifiable by downstream services
- [ ] OAuth client record creatable via API or migration seed for dev
- [ ] Documentation describes claim names and validation expectations

### Out of scope for Level 2

- Visual policy editor / ABAC rule engine UI
- Per-tenant billing or usage metering
- External IAM sync (LDAP/SCIM) — consider post–Level 3

---

## Level 3 — Protocols (future: v1.0+)

Epic: [#21](https://github.com/SydLabs9/identity-service/issues/21)

### In scope (directional)

- OAuth2 authorization code + refresh for web apps
- OIDC discovery (`/.well-known/openid-configuration`)
- Optional federated login (Google/GitHub) as authenticator implementations
- PKCE, client authentication best practices

### Acceptance criteria (draft)

- [ ] Third-party app completes authorization code flow against this service
- [ ] ID token validates per OIDC; JWKS and discovery documented
- [ ] Security review checklist completed for public client flows

---

## Cross-cutting scope (all phases)

| Area | In scope | Notes |
|------|----------|-------|
| **Infra** | CI, Docker, Pages, semver releases | [#19](https://github.com/SydLabs9/identity-service/issues/19), [#22](https://github.com/SydLabs9/identity-service/issues/22) |
| **Documentation** | README, `docs/`, `product/`, OpenAPI, architecture PNGs | Keep vision/scope aligned when phases ship |
| **Agent workflow** | Issue-driven development, PR template | See `.cursor/rules/issue-management.mdc` |

---

## Explicit non-goals (project-wide)

These are **not** planned unless explicitly re-opened in a future scope review:

1. **Replace enterprise IdP** (Okta, Azure AD) for large org SSO in v1
2. **Store payment, PII beyond identity**, or GDPR export tooling as core features
3. **GraphQL or gRPC-first API** — HTTP JSON remains primary; adapters optional later
4. **Built-in UI** (login pages, admin console) — consumers bring their own frontends until protocol layer needs hosted pages
5. **Blockchain / decentralized identity** experiments in the core repo

---

## Branch and contribution scope

| Branch | Purpose |
|--------|---------|
| **`main`** | Stable; only release merges; tagged SemVer |
| **`dev`** | Integration; feature PRs land here |

Contributors work on [sydlab/identity-service](https://github.com/sydlab/identity-service) (or forks) and open PRs **`→ SydLabs9/identity-service:dev`**. Release PRs merge **`dev → main`** on the org repo. See [CONTRIBUTING.md](../CONTRIBUTING.md).

---

## Scope change process

1. Open a GitHub Issue labeled `roadmap` with proposed change and level impact.
2. Update this file and [VISION.md](VISION.md) in the same PR if accepted.
3. Add or adjust milestone and project board item on [SydLabs9 Project 1](https://github.com/orgs/SydLabs9/projects/1/views/1).
