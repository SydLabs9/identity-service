# Aurora PostgreSQL Grants Runbook

This runbook configures least-privilege DB access for the identity service.

## Preconditions

- Aurora PostgreSQL cluster/database exists.
- You have an admin user (or equivalent elevated role) for the target database.
- Network access to cluster endpoint is available from your execution environment.

## Files

- `grants.sql`: role/user/grant bootstrap script.
- `../liquibase`: Liquibase changelog files that application startup and migration workflows use.

## Execution order

1. Open `grants.sql`.
2. Replace `REPLACE_ME_STRONG_PASSWORD` with a secure value, or adapt to your password/IAM auth strategy.
3. Execute the script as an admin user:

   ```bash
   psql "host=<aurora-endpoint> port=5432 dbname=<db> user=<admin> sslmode=require" -f database/aurora/grants.sql
   ```

4. Store `identity_app_user` credentials in secrets manager.
5. Configure application environment variables:
   - `SPRING_PROFILES_ACTIVE=prod`
   - `DB_URL=jdbc:postgresql://<aurora-endpoint>:5432/<db>?sslmode=require`
   - `DB_USERNAME=identity_app_user`
   - `DB_PASSWORD=<secret>`

## Verification

Run these checks as the app user:

```sql
SELECT current_user;
SELECT has_schema_privilege(current_user, 'public', 'USAGE');
SELECT has_table_privilege(current_user, 'public.users', 'SELECT');
```

Then start the service in prod profile and confirm Liquibase migrations + register/login flows succeed.

## Notes

- Script is idempotency-light by design; run once per environment. For reruns, adjust with `IF NOT EXISTS` patterns according to your Aurora/PostgreSQL version and policy.
- If your org forbids `CREATE` in `public`, move Liquibase objects into a dedicated schema and update grants/config accordingly.
