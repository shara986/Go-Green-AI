package com.gogreen.ai.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationDate;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpirationDate;

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication.getName(), jwtExpirationDate);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication.getName(), refreshExpirationDate);
    }

    private String generateToken(String username, long expirationMillis) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + expirationMillis);

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parse(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public long getAccessTokenExpirationInSeconds() {
        return jwtExpirationDate / 1000;
    }
}
