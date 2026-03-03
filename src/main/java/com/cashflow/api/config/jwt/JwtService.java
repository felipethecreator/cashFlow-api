package com.cashflow.api.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.cashflow.api.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Algorithm algorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String generateToken(User user) {

        return JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(algorithm());
    }

    public String getSubject(String token) {
        return buildVerifier().verify(token).getSubject();
    }

    public boolean isValid(String token) {
        try {
            buildVerifier().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private JWTVerifier buildVerifier() {
        return JWT.require(algorithm())
                .build();
    }
}
