package org.withtime.be.withtimebe.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.*;
import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.auth.service.command.OAuth2CommandService;

@RestController
@RequestMapping("/api/v1/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2CommandService oAuth2CommandService;

    @GetMapping("/callback/{provider}")
    public DefaultResponse<OAuth2ResponseDTO.Login> loginWithOAuth2(HttpServletRequest request, HttpServletResponse response, @PathVariable String provider, @RequestParam String code) {
        return DefaultResponse.ok(oAuth2CommandService.login(request, response, provider, code));
    }
}
