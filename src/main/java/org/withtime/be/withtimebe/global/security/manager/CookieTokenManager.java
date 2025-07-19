package org.withtime.be.withtimebe.global.security.manager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.auth.dto.response.AuthResponseDTO;
import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.auth.service.command.TokenCommandService;
import org.withtime.be.withtimebe.domain.auth.service.command.TokenStorageCommandService;
import org.withtime.be.withtimebe.domain.auth.service.query.TokenQueryService;
import org.withtime.be.withtimebe.global.security.constants.AuthenticationConstants;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;
import org.withtime.be.withtimebe.global.util.CookieUtil;

@Component
@RequiredArgsConstructor
public class CookieTokenManager implements TokenManager {

    private final TokenStorageCommandService tokenStorageCommandService;
    private final TokenCommandService tokenCommandService;
    private final TokenQueryService tokenQueryService;

    @Override
    public void addToken(HttpServletRequest request, HttpServletResponse response, CustomUserDetails customUserDetails) {
        AuthResponseDTO.Login loginResponse = tokenCommandService.createLoginToken(customUserDetails);
        for (String token : new String[] {CookieUtil.getCookie(request, AuthenticationConstants.ACCESS_TOKEN_NAME), CookieUtil.getCookie(request, AuthenticationConstants.REFRESH_TOKEN_NAME)}) {
            if (token != null) {
                tokenStorageCommandService.addBlackList(token);
            }
        }

        CookieUtil.addCookie(request, response, AuthenticationConstants.ACCESS_TOKEN_NAME, loginResponse.accessToken(), (int) tokenQueryService.getAccessTokenExpiration().toSeconds());
        CookieUtil.addCookie(request, response, AuthenticationConstants.REFRESH_TOKEN_NAME, loginResponse.refreshToken(), (int) tokenQueryService.getRefreshTokenExpiration().toSeconds());
        tokenStorageCommandService.addRefreshToken(customUserDetails.getId(), loginResponse.refreshToken());
    }

}
