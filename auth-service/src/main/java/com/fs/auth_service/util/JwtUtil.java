package com.fs.auth_service.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    private final KeyPair keyPair;
    private final long expirationMs;

    public JwtUtil(KeyPair keyPair, @Value("${jwt.expiration-ms:3600000}") long expirationMs) {
        this.keyPair = keyPair;
        this.expirationMs = expirationMs;
    }

    public String generateToken(String username, List<String> roles) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .addClaims(Map.of("roles", roles))
                .signWith((RSAPrivateKey) keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey((RSAPublicKey) keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
