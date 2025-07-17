package org.withtime.be.withtimebe.domain.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.weather.service.command.WeatherTriggerService;
import org.withtime.be.withtimebe.domain.weather.dto.request.WeatherSyncReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
@Tag(name = "날씨 API", description = "관리자가 수동으로 날씨 데이터를 동기화하거나 추천 정보를 생성합니다.")
public class WeatherController {

    private final WeatherTriggerService weatherTriggerService;

    @PostMapping("/trigger")
    @Operation(summary = "수동 동기화 트리거 API by 지미 [Only Admin]",
            description = """
                    관리자가 수동으로 다음 중 하나의 작업을 실행합니다:
                    - SHORT_TERM: 단기 예보 데이터 수집
                    - MEDIUM_TERM: 중기 예보 데이터 수집
                    - ALL: 전체 동기화 작업 수행
                    ---
                    모든 작업은 비동기로 실행되며, 기존 데이터는 강제로 덮어씁니다.
                    targetRegionIds의 값이 null이면 등록된 모든 지역을 대상으로 작업을 실행합니다.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "트리거 성공 (비동기 작업 시작됨)", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - 잘못된 jobType 입력 (SHORT_TERM, MEDIUM_TERM 등만 허용됨)
                            """),
            @ApiResponse(responseCode = "403",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER403_0: 접근 권한이 없습니다.
                            - WEATHER403_1: 관리자만 접근할 수 있습니다.
                            """)
    })
    public DefaultResponse<WeatherSyncResDTO.ManualTriggerResult> manualTrigger(
            @Valid @RequestBody WeatherSyncReqDTO.ManualTrigger request) {

        log.info("수동 트리거 요청: jobType={}, targetRegionIds={}",
                request.jobType(), request.targetRegionIds());

        WeatherSyncResDTO.ManualTriggerResult response = weatherTriggerService.triggerAsync(request);

        return DefaultResponse.ok(response);
    }
}
