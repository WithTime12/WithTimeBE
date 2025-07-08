package org.withtime.be.withtimebe.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.*;
import org.withtime.be.withtimebe.domain.auth.dto.request.AuthRequestDTO;
import org.withtime.be.withtimebe.domain.auth.service.command.AuthCommandService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "인증 API")
public class AuthController {

    private final AuthCommandService authCommandService;

    @Operation(summary = "회원가입 API by 요시", description = "최초 회원가입 시 필요한 정보를 포함하여 회원가입 진행")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "회원가입 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - AUTH400_1: 이미 존재하는 이메일입니다.
                            """
            )
    })
    @PostMapping("/sign-up")
    public DefaultResponse<String> signUp(@Valid @RequestBody AuthRequestDTO.SignUp request) {
        authCommandService.signUp(request);
        return DefaultResponse.noContent();
    }

    @Operation(summary = "로그인 API by 요시", description = "로그인 API")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그인 성공, 쿠키에 access token, refresh token 저장"),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - AUTH401_1: 로그인에 실패했습니다.
                            """
            )
    })
    @PostMapping("/login")
    public DefaultResponse<String> login(@RequestBody AuthRequestDTO.Login request) {
        // 필터에서 처리
        return null;
    }

    @Operation(summary = "Access Token 재발급 API by 요시", description = "토큰 재발급 API, Access Token이 존재하는 경우 black list 처리")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "토큰 재발급 성공, 쿠키에 access token 재발급"),
            @ApiResponse(
                    responseCode = "401",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다.
                            - TOKEN401_1: Refresh Token의 기간이 만료되었습니다.
                            - TOKEN401_2: Refresh Token이 유효하지 않습니다. 혹은 최근에 발급한 Refresh Token이 아닙니다.
                            """
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "- MEMBER404_1: 토큰의 정보로 사용자를 찾지 못했습니다."
            )
    })
    @PostMapping("/reissue")
    public DefaultResponse<String> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        authCommandService.reissueToken(request, response);
        return DefaultResponse.noContent();
    }

    @Operation(summary = "로그아웃 API by 요시", description = "로그아웃 API, Access Token, Refresh Token를 Black List 처리")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "로그아웃 성공, 쿠키에 Access Token, Refresh Token 삭제 및 Black list 처리"),
    })
    @PostMapping("/logout")
    public DefaultResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        authCommandService.logout(request, response);
        return DefaultResponse.noContent();
    }
}
