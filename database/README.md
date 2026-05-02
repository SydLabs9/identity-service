# Database Architecture (Identity Service)

This folder contains database assets for local development and Aurora-compatible deployments.

## What changed so far

- Register/login now persist user records in PostgreSQL.
- Schema evolution is managed via Liquibase scripts.
- Liquibase scripts are located under `database/liquibase`.
- Aurora provisioning support includes role/grant scripts under `database/aurora`.

## Folder layout

- `database/liquibase/` - schema changelogs and versioned DDL changesets
- `database/aurora/` - Aurora runbook and grant bootstrap SQL

## Liquibase scripts

- Master changelog: `database/liquibase/db.changelog-master.yaml`
- Changesets:
  - `database/liquibase/changes/001-create-users-table.yaml`
  - `database/liquibase/changes/002-add-users-indexes.yaml`

All current changesets use `author: sydlab`.

## Current schema (implemented)

The current implementation persists authentication users in a single table:

- `users`
  - `id` (UUID, PK)
  - `email` (VARCHAR(255), unique, required)
  - `password_hash` (VARCHAR(255), required)
  - `enabled` (BOOLEAN, required)

## ER diagram (current + extension-ready)

```mermaid
erDiagram
  users {
    UUID id PK
    VARCHAR email UK
    VARCHAR password_hash
    BOOLEAN enabled
  }

  user_profiles {
    UUID user_id PK,FK
    VARCHAR display_name
    VARCHAR phone
    TIMESTAMP created_at
    TIMESTAMP updated_at
  }

  auth_identities {
    UUID id PK
    UUID user_id FK
    VARCHAR provider_type
    VARCHAR provider_subject
    BOOLEAN active
    TIMESTAMP created_at
  }

  credentials_password {
    UUID user_id PK,FK
    VARCHAR password_hash
    TIMESTAMP password_updated_at
    BOOLEAN must_rotate
  }

  mfa_factors {
    UUID id PK
    UUID user_id FK
    VARCHAR factor_type
    VARCHAR factor_ref
    BOOLEAN verified
    TIMESTAMP created_at
  }

  user_roles {
    UUID id PK
    UUID user_id FK
    VARCHAR role_name
    VARCHAR scope
    TIMESTAMP created_at
  }

  auth_sessions {
    UUID id PK
    UUID user_id FK
    VARCHAR session_state
    TIMESTAMP issued_at
    TIMESTAMP expires_at
    TIMESTAMP revoked_at
  }

  users ||--|| user_profiles : has
  users ||--o{ auth_identities : links
  users ||--|| credentials_password : owns
  users ||--o{ mfa_factors : enrolls
  users ||--o{ user_roles : assigned
  users ||--o{ auth_sessions : creates
```

## Extensibility guidance

- Keep `users` as the stable identity anchor.
- Add new authentication/security capabilities in separate tables and changesets.
- Prefer additive migrations (new tables/columns/indexes) over destructive changes.
- Preserve current API behavior while expanding capabilities (MFA, social login, SSO, passwordless, token/session models).

## Local and runtime behavior

- Spring config points Liquibase at `file:database/liquibase/db.changelog-master.yaml`.
- Local dev/test execution should run from repository root so file-based changelog resolution works.
- For Aurora environment setup, follow `database/aurora/README.md`.
