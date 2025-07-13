package org.withtime.be.withtimebe.domain.auth.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.global.error.exception.TokenException;
import org.withtime.be.withtimebe.global.util.JwtUtil;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class JwtTokenQueryService implements TokenQueryService {

    private final JwtUtil jwtUtil;

    @Override
    public Long getUserId(String token) throws TokenException {
        return jwtUtil.getUserId(token);
    }

    @Override
    public Duration getAccessTokenExpiration() {
        return jwtUtil.getAccessExpiration();
    }

    @Override
    public Duration getRefreshTokenExpiration() {
        return jwtUtil.getRefreshExpiration();
    }
}