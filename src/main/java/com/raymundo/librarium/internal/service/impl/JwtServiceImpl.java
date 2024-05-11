package com.raymundo.librarium.internal.service.impl;

import com.raymundo.librarium.internal.service.JwtService;
import com.raymundo.librarium.internal.util.ServiceType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class JwtServiceImpl implements JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

    private static final String SERVICE_NAME_CLAIM = "service_name";
    private static final String SERVICE_AUTHORITY_CLAIM = "service_authority";

    private final String verifyingSecretKey;
    private final Map<ServiceType, String> signingSecretKeys;

    public JwtServiceImpl(String verifyingSecretKey, Map<ServiceType, String> signingSecretKeys) {
        this.verifyingSecretKey = verifyingSecretKey;
        this.signingSecretKeys = signingSecretKeys;
    }

    @Override
    public String generateToken(ServiceType from, ServiceType to) {
        var issuedAt = Instant.now();
        var expiration = issuedAt.plusSeconds(600);
        var signingSecretKey = signingSecretKeys.get(to);
        return Jwts.builder().claims()
                .add(SERVICE_NAME_CLAIM, from.getServiceName())
                .add(SERVICE_AUTHORITY_CLAIM, from.getAuthority().getAuthority())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .and()
                .signWith(getKey(signingSecretKey))
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.info(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public String getServiceName(String token) {
        return getClaim(token, SERVICE_NAME_CLAIM, String.class);
    }

    @Override
    public String getServiceAuthority(String token) {
        return getClaim(token, SERVICE_AUTHORITY_CLAIM, String.class);
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey(verifyingSecretKey))
                .build()
                .parseSignedClaims(token);
    }

    private <T> T getClaim(String token, String claim, Class<T> type) {
        return parseClaims(token)
                .getPayload()
                .get(claim, type);
    }

    private SecretKey getKey(String secretKey) {
        var keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
