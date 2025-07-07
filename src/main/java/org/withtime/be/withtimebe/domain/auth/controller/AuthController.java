package org.withtime.be.withtimebe.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.*;
import org.withtime.be.withtimebe.domain.auth.dto.request.AuthRequestDTO;
import org.withtime.be.withtimebe.domain.auth.service.command.AuthCommandService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthCommandService authCommandService;

    @PostMapping("/sign-up")
    public DefaultResponse<String> signUp(@Valid @RequestBody AuthRequestDTO.SignUp request) {
        authCommandService.signUp(request);
        return DefaultResponse.noContent();
    }

    @PostMapping("/login")
    public DefaultResponse<String> login(@RequestBody AuthRequestDTO.Login request) {
        // 필터에서 처리
        return null;
    }
}
