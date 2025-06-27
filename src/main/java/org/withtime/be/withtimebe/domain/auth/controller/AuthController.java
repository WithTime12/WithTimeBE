package org.withtime.be.withtimebe.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.*;
import org.withtime.be.withtimebe.domain.auth.dto.request.AuthRequest;
import org.withtime.be.withtimebe.domain.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public DefaultResponse<String> signUp(@RequestBody AuthRequest.SignUpRequest request) {
        authService.signUp(request);
        return DefaultResponse.noContent();
    }
}
