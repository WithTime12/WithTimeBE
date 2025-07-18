package org.withtime.be.withtimebe.domain.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.withtime.be.withtimebe.domain.weather.data.service.WeatherRecommendationGenerationService;
import org.withtime.be.withtimebe.domain.weather.dto.request.WeatherReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherResDTO;
import org.withtime.be.withtimebe.domain.weather.service.command.WeatherTriggerService;
import org.withtime.be.withtimebe.domain.weather.dto.request.WeatherSyncReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/weather")
@Tag(name = "날씨 API", description = "관리자가 수동으로 날씨 데이터를 동기화하거나 추천 정보를 생성합니다.")
public class WeatherController {

    private final WeatherTriggerService weatherTriggerService;
    private final WeatherRecommendationGenerationService weatherRecommendationGenerationService;

    @PostMapping("/trigger")
    @Operation(summary = "수동 동기화 트리거 API by 지미 [Only Admin]",
            description = """
                    관리자가 수동으로 다음 중 하나의 작업을 실행합니다:
                    - SHORT_TERM: 단기 예보 데이터 수집
                    - MEDIUM_TERM: 중기 예보 데이터 수집
                    - RECOMMENDATION: 날씨 기반 추천 생성
                    - CLEANUP: 오래된 날씨 데이터 삭제
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

    @GetMapping("/{regionId}/weekly")
    @Operation(
            summary = "지역별 주간 날씨 기반 추천 조회",
            description = """
        특정 지역의 7일치(오늘 기준) 날씨 데이터를 바탕으로 한 데이트 추천 정보를 제공합니다.

        - 날짜 범위: `startDate`부터 7일간 (startDate 포함)
        - 추천 데이터는 날씨 분류 후 템플릿 기반으로 생성됩니다.
        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주간 추천 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 형식 (날짜 혹은 지역 ID 오류)"),
            @ApiResponse(responseCode = "404", description = "해당 지역의 추천 정보가 존재하지 않음")
    })
    public DefaultResponse<WeatherResDTO.WeeklyRecommendation> getWeeklyRecommendation(
            @Parameter(description = "지역 ID)", required = true)
            @PathVariable @NotNull @Positive Long regionId,

            @Parameter(description = "조회 시작일 (YYYY-MM-DD)", required = true, example = "2025-07-17")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

        log.info("주간 날씨 추천 조회 API 호출: regionId={}, startDate={}", regionId, startDate);
        WeatherReqDTO.GetWeeklyRecommendation request = WeatherReqDTO.GetWeeklyRecommendation.of(regionId, startDate);
        WeatherResDTO.WeeklyRecommendation response = weatherRecommendationGenerationService.getWeeklyRecommendation(request);
        return DefaultResponse.ok(response);
    }

    @GetMapping("/{regionId}/precipitation")
    @Operation(
            summary = "지역별 7일간 강수확률 조회",
            description = """
    특정 지역의 7일간 강수확률 정보만 간단하게 조회합니다.
    
    - 날짜 범위: `startDate`부터 7일간 (startDate 포함)
    - 중기예보 데이터를 우선적으로 사용하고, 없을 경우 단기예보 데이터 사용
    - 각 날짜별 강수확률과 주간 평균, 경향 분석 제공
    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강수확률 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "잘못된 파라미터 형식 (날짜 혹은 지역 ID 오류)"),
            @ApiResponse(responseCode = "404", description = "해당 지역이 존재하지 않음")
    })
    public DefaultResponse<WeatherResDTO.WeeklyPrecipitation> getWeeklyPrecipitation(
            @Parameter(description = "지역 ID", required = true)
            @PathVariable @NotNull @Positive Long regionId,

            @Parameter(description = "조회 시작일 (YYYY-MM-DD)", required = true, example = "2025-07-18")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

        log.info("7일간 강수확률 조회 API 호출: regionId={}, startDate={}", regionId, startDate);

        WeatherReqDTO.GetWeeklyPrecipitation request = WeatherReqDTO.GetWeeklyPrecipitation.of(regionId, startDate);
        WeatherResDTO.WeeklyPrecipitation response = weatherRecommendationGenerationService.getWeeklyPrecipitation(request);

        return DefaultResponse.ok(response);
    }
}
