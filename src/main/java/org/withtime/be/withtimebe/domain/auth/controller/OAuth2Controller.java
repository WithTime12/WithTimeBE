package org.withtime.be.withtimebe.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "소셜 로그인 API")
public class OAuth2Controller {

    private final OAuth2CommandService oAuth2CommandService;

    @Operation(summary = "소셜 로그인 API", description = "/oauth2/authorization/{provider}로 서버에 요청을 보낸 뒤 리다이렉트된 URI의 코드를 사용하여 요청, 리다이렉트되는 URI 의 Endpoint는 해당 API와 동일합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = """
                            소셜 로그인 성공
                            - isFirst: true 시 최초 회원가입 필요, 이메일은 인증된 상태로 1시간 유효
                            - isFirst: false 시 최초 회원가입 필요 X, 로그인 처리
                            """
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - AUTH400_1: 지원하지 않는 소셜 로그인입니다. provider가 잘못되었거나 지원하지 않는 provider입니다.
                            """
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - AUTH500_1: 사용자 정보를 가져오는데 실패했습니다. 인가코드가 잘못되었거나 OAuth2 인증 서버나 리소스 서버에 보낸 요청이 실패했습니다.
                            """
            ),

    })
    @GetMapping("/callback/{provider}")
    public DefaultResponse<OAuth2ResponseDTO.Login> loginWithOAuth2(HttpServletRequest request, HttpServletResponse response,
                                                                    @Parameter(description = "소셜 로그인 플랫폼(대소문자 상관 없음), [kakao, google, naver]", example = "kakao") @PathVariable String provider,
                                                                    @RequestParam String code) {
        return DefaultResponse.ok(oAuth2CommandService.login(request, response, provider, code));
    }
}
