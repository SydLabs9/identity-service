# Application architecture

## Layering

The codebase follows a simple layered style:

| Layer | Package | Responsibility |
|-------|---------|----------------|
| HTTP | `com.example.identity.auth` | REST DTOs, `AuthController` |
| Domain / application | `com.example.identity.user` | `UserService`, orchestration, transactions |
| Persistence | `com.example.identity.user` | JPA `User`, `UserRepository` |
| Cross-cutting | `com.example.identity.config` | `SecurityFilterChain`, `PasswordEncoder` bean |
| API errors | `com.example.identity.web` | `ApiExceptionHandler` (`@RestControllerAdvice`) |

`PasswordAuthenticator` lives in `auth` and delegates to `UserService` so the controller depends on **`Authenticator`**, not on transport-agnostic orchestration directly.

```mermaid
flowchart TB
    Client[HTTP client]
    AC[AuthController]
    AU[Authenticator]
    US[UserService]
    REPO[UserRepository]
    ENC[PasswordEncoder]
    Client -->|"POST register/login"| AC
    AC --> US
    AC --> AU
    AU --> US
    US --> REPO
    US --> ENC
```

## Registration flow

```mermaid
sequenceDiagram
    participant C as Client
    participant CTL as AuthController
    participant S as UserService
    participant R as UserRepository
    participant E as PasswordEncoder
    C->>CTL: POST /api/auth/register
    CTL->>S: register(email, password)
    S->>R: existsByEmailIgnoreCase?
    alt email taken
        S-->>CTL: UserAlreadyExistsException
        CTL-->>C: 409 Conflict
    else new user
        S->>E: encode(password)
        S->>R: save(User)
        S-->>CTL: User
        CTL-->>C: 201 + userId, email
    end
```

## Login flow

```mermaid
sequenceDiagram
    participant C as Client
    participant CTL as AuthController
    participant A as PasswordAuthenticator
    participant S as UserService
    participant R as UserRepository
    participant E as PasswordEncoder
    C->>CTL: POST /api/auth/login
    CTL->>A: authenticate(email, password)
    A->>S: authenticate(email, password)
    S->>R: findByEmailIgnoreCase
    alt not found or disabled or bad password
        S-->>CTL: InvalidCredentialsException
        CTL-->>C: 401 Unauthorized
    else ok
        S-->>CTL: User
        CTL-->>C: 200 + userId, email
    end
```

## Security configuration

Spring Security:

- **`/api/auth/**`** is permitted without authentication (stateless, no session, CSRF disabled for this JSON API surface).
- **Everything else** requires authentication — today most traffic is auth endpoints; additional routes remain protected by default.

Passwords are never returned in responses; only a hash is stored (`password_hash` column).

## Error contract

Domain exceptions map to HTTP status in **`ApiExceptionHandler`**:

| Exception | HTTP | Typical `message` |
|-----------|------|-------------------|
| `UserAlreadyExistsException` | 409 | User already exists with this email |
| `InvalidCredentialsException` | 401 | Invalid credentials |
| `MethodArgumentNotValidException` | 400 | Validation failed (+ `details` field errors) |

## Extension seam: Authenticator

New credential checks (MFA steps, federation, magic links) should:

1. Implement `Authenticator` (or a richer interface layered on it).
2. Wire the bean used by `AuthController` without changing repository contracts unnecessarily.
3. Add tests parallel to existing integration and unit tests.
