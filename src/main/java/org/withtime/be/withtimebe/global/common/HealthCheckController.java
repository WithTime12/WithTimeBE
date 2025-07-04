package org.withtime.be.withtimebe.global.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

//    @Operation(summary = "회원가입 API", description = "새로운 사용자를 추가합니다")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
//            @ApiResponse(
//                    responseCode = "400",
//                    description = """
//                        다음과 같은 이유로 실패할 수 있습니다:
//                        - AUTH_015: 이미 있는 유저입니다.
//                        - AUTH_016: 널이어서는 안 됩니다.
//                        - AUTH_017: 올바른 형식의 이메일 주소여야 합니다.
//                        - AUTH_018: 길이가 1에서 30 사이여야 합니다.
//                    """
//            )
//    })

    @Operation(summary = "헬스 체킹 API by 제이미", description = "서버 상태 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공입니다.")
    })
    @GetMapping("/health")
    public DefaultResponse<String> healthCheck() {
        return DefaultResponse.ok("I am healthy");
    }
}
