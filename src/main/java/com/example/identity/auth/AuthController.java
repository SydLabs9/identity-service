package com.example.identity.auth;

import com.example.identity.auth.AuthDtos.AuthUserResponse;
import com.example.identity.auth.AuthDtos.LoginRequest;
import com.example.identity.auth.AuthDtos.RegisterRequest;
import com.example.identity.user.User;
import com.example.identity.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final Authenticator authenticator;

    public AuthController(UserService userService, Authenticator authenticator) {
        this.userService = userService;
        this.authenticator = authenticator;
    }

    @Operation(summary = "Register user")
    @ApiResponse(responseCode = "201", description = "User created")
    @ApiResponse(
        responseCode = "400",
        description = "Validation failed",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(
        responseCode = "409",
        description = "Email already registered",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUserResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request.email(), request.password());
        return new AuthUserResponse(user.getId().toString(), user.getEmail());
    }

    @Operation(summary = "Login")
    @ApiResponse(responseCode = "200", description = "Credentials valid")
    @ApiResponse(
        responseCode = "400",
        description = "Validation failed",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(
        responseCode = "401",
        description = "Invalid credentials",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @PostMapping("/login")
    public AuthUserResponse login(@Valid @RequestBody LoginRequest request) {
        User user = authenticator.authenticate(request.email(), request.password());
        return new AuthUserResponse(user.getId().toString(), user.getEmail());
    }
}
