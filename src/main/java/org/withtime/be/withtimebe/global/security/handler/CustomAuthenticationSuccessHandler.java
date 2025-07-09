package org.withtime.be.withtimebe.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.auth.dto.response.AuthResponseDTO;
import org.withtime.be.withtimebe.domain.auth.service.command.TokenCommandService;
import org.withtime.be.withtimebe.domain.auth.service.query.TokenQueryService;
import org.withtime.be.withtimebe.domain.auth.service.command.TokenStorageCommandService;
import org.withtime.be.withtimebe.global.security.constants.AuthenticationConstants;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;
import org.withtime.be.withtimebe.global.util.CookieUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenStorageCommandService tokenStorageCommandService;
    private final TokenCommandService tokenCommandService;
    private final TokenQueryService tokenQueryService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails customUserDetails =(CustomUserDetails) authentication.getPrincipal();
        AuthResponseDTO.Login loginResponse = tokenCommandService.createLoginToken(customUserDetails);
        addToken(request, response, loginResponse, customUserDetails);

        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getOutputStream(), DefaultResponse.noContent());
    }

    private void addToken(HttpServletRequest request, HttpServletResponse response, AuthResponseDTO.Login loginResponse, CustomUserDetails customUserDetails) {
        // 쿠키에 이미 토큰이 있는 경우 블랙리스트 처리
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
