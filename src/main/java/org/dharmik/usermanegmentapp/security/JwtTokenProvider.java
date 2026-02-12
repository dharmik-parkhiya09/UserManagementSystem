package org.dharmik.usermanegmentapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.dharmik.usermanegmentapp.Entity.Role;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.dharmik.usermanegmentapp.Entity.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private final long EXPIRATION_TIME = 86400000;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Object userId = parseClaims(token).get("userId");
        return userId instanceof Number ? ((Number) userId).longValue() : Long.parseLong(userId.toString());
    }

    public List<String> getRoleFromToken(String token) {
        List<?> roles = parseClaims(token).get("roles", List.class);
        if (roles == null) {
            throw new IllegalArgumentException("Roles not found in token");
        }
        return roles.stream().map(Object::toString).toList();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid or expired JWT: {}", e.getMessage());
            return false;
        }
    }


}
