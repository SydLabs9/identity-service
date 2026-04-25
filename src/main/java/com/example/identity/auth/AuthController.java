package com.example.identity.auth;

import com.example.identity.auth.AuthDtos.AuthUserResponse;
import com.example.identity.auth.AuthDtos.LoginRequest;
import com.example.identity.auth.AuthDtos.RegisterRequest;
import com.example.identity.user.User;
import com.example.identity.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final Authenticator authenticator;

    public AuthController(UserService userService, Authenticator authenticator) {
        this.userService = userService;
        this.authenticator = authenticator;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthUserResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request.email(), request.password());
        return new AuthUserResponse(user.getId().toString(), user.getEmail());
    }

    @PostMapping("/login")
    public AuthUserResponse login(@Valid @RequestBody LoginRequest request) {
        User user = authenticator.authenticate(request.email(), request.password());
        return new AuthUserResponse(user.getId().toString(), user.getEmail());
    }
}
