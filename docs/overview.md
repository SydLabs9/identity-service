# Overview

![Level 0 — current implementation](./architecture-level-0-current.png)

Level 0 matches the code today: REST register/login, hashed passwords, Liquibase on PostgreSQL.

For the full multi-protocol / JWT / JWKS vision (not implemented yet), see [future-platform diagram](./architecture-future-platform.png).

## Purpose

Centralize identity: register users, store credentials safely, verify passwords over HTTP. Stronger protocols (JWT, OAuth2, federation) are intended to plug in later around a stable **`User`** model ([root README](../README.md) roadmap).

## In scope vs out (today)

| In scope | Out of scope (by design until later phases) |
|----------|---------------------------------------------|
| `User` lifecycle, Postgres + Liquibase, `Authenticator` abstraction | Tokens, JWKS, refresh, OAuth/OIDC surfaces, federation |

## Principles

1. Keep **`User`** and repositories stable as contracts evolve upward.
2. Add proof mechanisms via **`Authenticator`** and related beans, not by hard-wiring transports into `UserService`.
3. Prefer **PostgreSQL + Liquibase** with `ddl-auto=validate`.

## Bundling vs splitting

One deployable with strict packages (`user`, `auth`, `config`, `web`). Split services only when operations or compliance clearly require it.
