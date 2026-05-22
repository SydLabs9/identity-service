# Release process

This project treats **`main`** as the stable line and **`dev`** as integration. Tags on `main` mark portfolio-ready milestones.

## Branch flow

```text
feature/*  →  PR  →  dev  →  PR  →  main  →  tag vX.Y.Z  →  GitHub Release
```

1. Develop on a short-lived branch from **`dev`**.
2. Open a PR into **`dev`**; ensure `./gradlew test` passes.
3. When a milestone is ready (for example database-backed identity), open a PR **`dev` → `main`**.
4. After merge to **`main`**, tag and publish (below).

## Before you tag

- Move items from **`[Unreleased]`** in [`CHANGELOG.md`](../CHANGELOG.md) into a new **`[X.Y.Z] - YYYY-MM-DD`** section with a one-line **Theme**.
- Keep bullets **purpose-driven** (what capability shipped), not a file list.
- Confirm [`README.md`](../README.md) **Releases** table matches the new tag.

## Tag and GitHub Release

Replace `X.Y.Z` and the theme message:

```bash
git checkout main
git pull origin main

git tag -a vX.Y.Z -m "vX.Y.Z — short theme sentence"
git push origin vX.Y.Z
```

Create the GitHub Release (attach notes from CHANGELOG):

```bash
gh release create vX.Y.Z --title "vX.Y.Z" --notes-file /tmp/release-notes.md
```

Or use **Releases → Draft a new release** in the GitHub UI and paste the CHANGELOG section.

## Version guide (SemVer)

| Bump | When |
|------|------|
| **MAJOR** | Breaking API or credential contract |
| **MINOR** | New capability (JWT, refresh, new endpoints) |
| **PATCH** | Fixes, docs-only, non-breaking dependency updates |

## Shipped milestones

| Tag | Theme |
|-----|--------|
| [v0.1.0](https://github.com/sydlab/identity-service/releases/tag/v0.1.0) | Auth foundation (register/login) |
| [v0.2.0](https://github.com/sydlab/identity-service/releases/tag/v0.2.0) | Database-backed identity (Postgres, Liquibase, Docker) |
| [v0.3.0](https://github.com/sydlab/identity-service/releases/tag/v0.3.0) | Published OpenAPI / Redoc on GitHub Pages |

Roadmap items not in a tag yet live in [ROADMAP.md](ROADMAP.md) and [GitHub Issues](https://github.com/sydlab/identity-service/issues).
