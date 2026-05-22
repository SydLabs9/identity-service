# Vision

## One sentence

Build a **generic authentication and authorization platform** that Java/Spring services can trust in distributed systems — starting with password identity and evolving toward tokens, policy, and standard protocols.

## Why this exists

Microservices and modular backends need a **single place** for identity lifecycle, credential verification, and (eventually) access decisions. Ad-hoc auth in every service leads to inconsistent security, duplicated user stores, and painful upgrades when requirements change (MFA, SSO, machine clients, fine-grained permissions).

This project is a **deliberately layered foundation**: stable identity domain at the core, pluggable authentication mechanisms, and protocol adapters added only when the previous layer is solid.

## Who it is for

| Audience | What they get |
|----------|----------------|
| **Service developers** | A small, well-documented HTTP API and (from v0.4+) JWT validation via JWKS — no need to own password hashing or token signing in every app |
| **Platform / SRE** | Postgres-backed identity, Docker compose for local parity, OpenAPI docs, semver releases on **`main`** |
| **SydLabs9 portfolio** | A credible AuthN/AuthZ story that grows from demo-ready (Level 0) to production-shaped (Levels 1–3) |

## Maturity levels

We describe progress in **levels**, not just version numbers. Each level unlocks the next without rewriting the core.

```text
Level 0 ──► Level 1 ──► Level 2 ──► Level 3
(current)     tokens      authz         protocols
```

| Level | Name | Capability | Distributed-systems meaning |
|-------|------|------------|-----------------------------|
| **0** | **Foundation** | Register/login, Postgres identity, API docs | Services can delegate **credential checks** to one service; no cross-service token trust yet |
| **1** | **Tokens** | JWT access/refresh, JWKS, protected `/api/me` | Other services can **validate Bearer tokens** without calling identity on every request |
| **2** | **Authorization data** | Roles, OAuth clients, policy hooks | Services can ask **who** and **what they may do** with structured claims and client registry |
| **3** | **Protocols** | OAuth2/OIDC gateway, federated IdP | Browsers and third-party apps use **standard flows**; identity service becomes a true IdP |

Diagrams: [Level 0](../docs/architecture-level-0-current.png) · [Future platform](../docs/architecture-future-platform.png)

## Design principles

1. **Stable identity domain** — `User`, lifecycle, and credential state change slowly; auth mechanisms change faster.
2. **Extension seams, not big-bang rewrites** — `Authenticator` today; token issuers and policy engines later behind clear interfaces.
3. **Fail closed** — Invalid credentials and tokens reject; no silent downgrade to anonymous access on protected routes.
4. **Document the contract** — OpenAPI and semver tags so consumers know what is stable vs experimental.
5. **Small, shippable increments** — Each release has a one-line **Theme** and closed issues; roadmap items stay on **`dev`** until tagged on **`main`**.

## Success looks like

- **Short term (v0.4.x):** A developer registers a user, logs in, receives JWTs, hits `/api/me` with a Bearer token, and another Spring service validates the JWT using JWKS — all documented in Redoc.
- **Medium term (v0.5–v0.9):** Roles and client records exist; services map claims to authorization without embedding user tables.
- **Long term (v1.0+):** OAuth2/OIDC flows for web and machine clients; optional federation — still one cohesive identity platform, not a grab bag of scripts.

## What we are not trying to be (yet)

- A full **Keycloak/Auth0 replacement** on day one
- A **user profile / CRM** system (avatars, marketing prefs, etc.)
- **Session-based SSO for legacy monoliths** before token layer lands
- **Custom crypto** — we use battle-tested libraries and standard algorithms

See [SCOPE.md](SCOPE.md) for phased acceptance criteria and explicit non-goals.

## Relationship to SydLabs9

This repo is the **flagship identity component** in the [SydLabs9 project board](https://github.com/orgs/SydLabs9/projects/1/views/1). Work is tracked in GitHub Issues, synced to the board, and shipped via tagged releases on **`main`**. Personal development happens on [sydlab/identity-service](https://github.com/sydlab/identity-service) with PRs into **`SydLabs9/dev`**.
