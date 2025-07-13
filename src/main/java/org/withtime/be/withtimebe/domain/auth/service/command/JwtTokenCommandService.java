package org.withtime.be.withtimebe.domain.auth.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.auth.converter.AuthConverter;
import org.withtime.be.withtimebe.domain.auth.dto.response.AuthResponseDTO;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;
import org.withtime.be.withtimebe.global.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class JwtTokenCommandService implements TokenCommandService {

    private final JwtUtil jwtUtil;

    @Override
    public AuthResponseDTO.Login createLoginToken(CustomUserDetails customUserDetails) {
        return AuthConverter.toLoginResponse(
                jwtUtil.createAccessToken(customUserDetails),
                jwtUtil.createRefreshToken(customUserDetails)
        );
    }

    @Override
    public String reissueAccessToken(CustomUserDetails customUserDetails) {
        return jwtUtil.createAccessToken(customUserDetails);
    }
}
