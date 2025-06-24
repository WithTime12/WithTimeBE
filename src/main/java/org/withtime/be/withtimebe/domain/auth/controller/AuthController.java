package org.withtime.be.withtimebe.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.auth.dto.request.AuthRequest;
import org.withtime.be.withtimebe.domain.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody AuthRequest.SignUpRequest request) {
        authService.signUp(request);
        return "회원가입 성공";
    }
}
