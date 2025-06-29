package org.withtime.be.withtimebe.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.global.data.JwtConfigData;
import org.withtime.be.withtimebe.global.error.code.TokenErrorCode;
import org.withtime.be.withtimebe.global.error.exception.TokenException;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Duration accessExpiration;
    private final Duration refreshExpiration;

    public JwtUtil(JwtConfigData jwtConfigData) {
        this.secretKey = Keys.hmacShaKeyFor(jwtConfigData.getSecret().getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = Duration.ofMillis(jwtConfigData.getTime().getAccessToken());
        this.refreshExpiration = Duration.ofMillis(jwtConfigData.getTime().getRefreshToken());
    }

    public String createAccessToken(CustomUserDetails details) {
        return createToken(details, accessExpiration);
    }

    public String createRefreshToken(CustomUserDetails details) {
        return createToken(details, refreshExpiration);
    }

    public Long getUserId(String token) {
        try {
            return getClaims(token).getPayload().get("id", Long.class);
        } catch (ExpiredJwtException e) {
            throw new TokenException(TokenErrorCode.TOKEN_EXPIRED);
        } catch (JwtException e) {
            return null;
        }
    }

    private String createToken(CustomUserDetails detail, Duration expiration) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(detail.getUsername())
                .claim("id", detail.getId())
                .claim("providerType", detail.getProviderType())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(secretKey)
                .compact();
    }

    private Jws<Claims> getClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token);
    }
}
