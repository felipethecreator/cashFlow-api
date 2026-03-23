package com.cashflow.api.common.security;

import com.cashflow.api.common.exceptions.UnauthorizedException;
import com.cashflow.api.user.entity.User;
import org.springframework.security.core.Authentication;

public final class AuthenticatedUser {

    private AuthenticatedUser() {
    }

    public static User require(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuário não autenticado.");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User user)) {
            throw new UnauthorizedException("Usuário não autenticado.");
        }

        return user;
    }
}
