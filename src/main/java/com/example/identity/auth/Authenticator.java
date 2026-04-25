package com.example.identity.auth;

import com.example.identity.user.User;

public interface Authenticator {
    User authenticate(String principal, String secret);
}
