package com.shopsphere.order.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // keep secret in env/CONFIG in prod
    private static final String SECRET = "verysecureverysecureverysecure1234!!";

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            res.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing Authorization header");
            return;
        }

        String token = header.substring(7);
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();

            // subject is userId (string)
            String subject = Optional.ofNullable(claims.getSubject()).orElseThrow();
            Long userId = Long.valueOf(subject);

            String username = claims.get("username", String.class);
            String role = claims.get("role", String.class); // "ADMIN" or "USER"
            if (role == null) {
                res.sendError(HttpStatus.FORBIDDEN.value(), "Role missing in token");
                return;
            }

            // map to Spring authority
            var auth = new UsernamePasswordAuthenticationToken(
                    userId.toString(), // principal = userId string
                    null, // credentials not needed
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            // make user info available to controllers via request attributes if desired:
            req.setAttribute("userId", userId);
            req.setAttribute("username", username);
            req.setAttribute("role", role);

            chain.doFilter(req, res);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            res.sendError(HttpStatus.UNAUTHORIZED.value(), "Token expired");
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            res.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token: " + e.getMessage());
        }
    }
}

