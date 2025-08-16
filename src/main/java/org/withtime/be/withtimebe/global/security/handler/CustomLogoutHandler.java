package org.withtime.be.withtimebe.global.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.auth.service.command.TokenStorageCommandService;
import org.withtime.be.withtimebe.domain.auth.service.query.TokenQueryService;
import org.withtime.be.withtimebe.domain.auth.service.query.TokenStorageQueryService;
import org.withtime.be.withtimebe.global.security.constants.AuthenticationConstants;
import org.withtime.be.withtimebe.global.util.CookieUtil;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler {

    private final TokenStorageCommandService tokenStorageCommandService;
    private final TokenStorageQueryService tokenStorageQueryService;
    private final TokenQueryService tokenQueryService;

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = getAccessToken(request);
        String refreshToken = getRefreshToken(request);

        tokenStorageCommandService.addBlackList(accessToken);
        tokenStorageCommandService.addBlackList(refreshToken);

        // 쿠키의 Refresh Token이 다른 경우를 대비해 Redis Refresh도 Black list 처리
        Long userId = getUserId(refreshToken);
        tokenStorageCommandService.addBlackList(tokenStorageQueryService.getRefreshToken(userId));
        tokenStorageCommandService.deleteRefreshToken(userId);

        CookieUtil.deleteCookie(request, response, AuthenticationConstants.ACCESS_TOKEN_NAME);
        CookieUtil.deleteCookie(request, response, AuthenticationConstants.REFRESH_TOKEN_NAME);
    }

    private Long getUserId(String token) {
        return tokenQueryService.getUserId(token);
    }

    private String getAccessToken(HttpServletRequest request) {
        return CookieUtil.getCookie(request, AuthenticationConstants.ACCESS_TOKEN_NAME);
    }

    private String getRefreshToken(HttpServletRequest request) {
        return CookieUtil.getCookie(request, AuthenticationConstants.REFRESH_TOKEN_NAME);
    }}
