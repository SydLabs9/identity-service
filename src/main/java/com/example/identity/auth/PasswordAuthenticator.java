package com.example.identity.auth;

import com.example.identity.user.User;
import com.example.identity.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class PasswordAuthenticator implements Authenticator {

    private final UserService userService;

    public PasswordAuthenticator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User authenticate(String principal, String secret) {
        return userService.authenticate(principal, secret);
    }
}
