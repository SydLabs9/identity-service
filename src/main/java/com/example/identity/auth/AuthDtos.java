package com.example.identity.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDtos {
    private AuthDtos() {
    }

    @Schema(description = "Registration request body")
    public record RegisterRequest(
        @Schema(description = "Canonical email address", example = "user@example.com")
        @NotBlank @Email String email,

        @Schema(description = "Plain password; never echoed in responses", accessMode = Schema.AccessMode.WRITE_ONLY, minLength = 8, maxLength = 128)
        @NotBlank @Size(min = 8, max = 128) String password
    ) {
    }

    @Schema(description = "Login request body")
    public record LoginRequest(
        @Schema(description = "Account email", example = "user@example.com")
        @NotBlank @Email String email,

        @Schema(description = "Plain password", accessMode = Schema.AccessMode.WRITE_ONLY)
        @NotBlank String password
    ) {
    }

    @Schema(description = "Subset of identity returned after register or login")
    public record AuthUserResponse(
        @Schema(description = "User identifier (UUID as string)")
        String userId,
        @Schema(description = "Verified email stored for the account", example = "user@example.com")
        String email
    ) {
    }
}
