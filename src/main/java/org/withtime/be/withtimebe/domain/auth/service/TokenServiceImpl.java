package org.withtime.be.withtimebe.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.auth.dto.response.AuthResponse;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;
import org.withtime.be.withtimebe.global.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse.LoginResponse createLoginToken(CustomUserDetails customUserDetails) {
        return AuthResponse.LoginResponse.builder()
                .accessToken(jwtUtil.createAccessToken(customUserDetails))
                .refreshToken(jwtUtil.createRefreshToken(customUserDetails))
                .build();
    }
}
