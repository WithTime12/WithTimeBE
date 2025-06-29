package org.withtime.be.withtimebe.domain.auth.controller;

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
    public DefaultResponse<String> signUp(@RequestBody AuthRequestDTO.SignUp request) {
        authCommandService.signUp(request);
        return DefaultResponse.noContent();
    }

    @GetMapping
    public void d() {
        throw new IllegalArgumentException("비상");
    }
}
