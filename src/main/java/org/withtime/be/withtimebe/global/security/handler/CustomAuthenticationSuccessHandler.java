package org.withtime.be.withtimebe.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.auth.dto.response.AuthResponse;
import org.withtime.be.withtimebe.domain.auth.service.TokenService;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        AuthResponse.LoginResponse loginResponse = tokenService.createLoginToken((CustomUserDetails) authentication.getPrincipal());
        // TODO: 응답 통일
        objectMapper.writeValue(response.getOutputStream(), loginResponse);
    }
}
