package org.withtime.be.withtimebe.domain.auth.service.query;

import org.withtime.be.withtimebe.global.error.exception.TokenException;

import java.time.Duration;

public interface TokenQueryService {
    Long getUserId(String token) throws TokenException;
    Duration getAccessTokenExpiration();
    Duration getRefreshTokenExpiration();
}
