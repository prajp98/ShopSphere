package com.shopsphere.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "secret";
    private final long EXPIRATION_TIME = 86400000;

    public String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser().verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser().verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date());
    }
}
