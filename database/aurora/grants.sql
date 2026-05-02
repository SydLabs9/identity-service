-- Aurora PostgreSQL privilege bootstrap for identity-service.
-- Run as an admin/DBA role in each environment (dev/stage/prod).

-- 1) Create a no-login role that owns application privileges.
CREATE ROLE identity_app_role NOLOGIN;

-- 2) Create login user for the application (or map this to an existing IAM/managed user).
-- Replace the password before execution in non-ephemeral environments.
CREATE USER identity_app_user WITH PASSWORD 'REPLACE_ME_STRONG_PASSWORD';

-- 3) Allow app user to assume app role.
GRANT identity_app_role TO identity_app_user;

-- 4) Schema permissions (public schema used by current Liquibase scripts).
GRANT USAGE ON SCHEMA public TO identity_app_role;
GRANT CREATE ON SCHEMA public TO identity_app_role;

-- 5) Table and sequence permissions for current and future objects.
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO identity_app_role;
GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA public TO identity_app_role;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO identity_app_role;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO identity_app_role;
