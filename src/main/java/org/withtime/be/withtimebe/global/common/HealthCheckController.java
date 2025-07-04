package org.withtime.be.withtimebe.global.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Operation(summary = "헬스 체킹 API by 김준환", description = "서버 상태 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공입니다.")
    })
    @GetMapping("/health")
    public DefaultResponse<String> healthCheck() {
        return DefaultResponse.ok("I am healthy");
    }
}
