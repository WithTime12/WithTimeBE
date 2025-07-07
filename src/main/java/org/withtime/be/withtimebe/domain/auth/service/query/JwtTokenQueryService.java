package org.withtime.be.withtimebe.domain.auth.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.global.util.JwtUtil;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class JwtTokenQueryService implements TokenQueryService {

    private final JwtUtil jwtUtil;

    @Override
    public Duration getAccessTokenExpiration() {
        return jwtUtil.getAccessExpiration();
    }

    @Override
    public Duration getRefreshTokenExpiration() {
        return jwtUtil.getRefreshExpiration();
    }
}