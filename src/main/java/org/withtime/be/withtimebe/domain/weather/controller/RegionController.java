package org.withtime.be.withtimebe.domain.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.withtime.be.withtimebe.domain.weather.dto.request.RegionReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.RegionResDTO;
import org.withtime.be.withtimebe.domain.weather.service.command.RegionCommandService;
import org.withtime.be.withtimebe.domain.weather.service.query.RegionQueryService;

@Slf4j
@RestController
@RequestMapping("/api/v1/regions")
@RequiredArgsConstructor
@Tag(name = "지역 관리 API", description = "지역 등록/관리 API")
public class RegionController {

    private final RegionCommandService regionCommandService;
    private final RegionQueryService regionQueryService;

    @PostMapping("/codes")
    @Operation(summary = "지역코드 등록 by 김지명", description = "새로운 지역코드를 등록합니다(관리자용).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "지역코드 등록 성공"),
            @ApiResponse(responseCode = "400",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER400_0: 이미 존재하는 지역입니다.
                            - WEATHER400_5: 올바르지 않은 지역코드입니다.
                            """),
            @ApiResponse(responseCode = "403",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER403_0: 접근 권한이 없습니다.
                            - WEATHER403_1: 관리자만 접근할 수 있습니다.
                            """)
    })
    @PreAuthorize("hasRole('ADMIN')")
    public DefaultResponse<RegionResDTO.CreateRegionCode> createRegionCode(
            @Valid @RequestBody RegionReqDTO.CreateRegionCode request) {
        log.info("지역코드 등록 API 호출: {}", request.name());

        RegionResDTO.CreateRegionCode response = regionCommandService.createRegionCode(request);
        return DefaultResponse.created(response);
    }

    @PostMapping
    @Operation(summary = "지역 등록 by 김지명",
            description = "기존 지역코드를 사용하여 새로운 지역을 등록합니다. 위경도는 자동으로 격자 좌표로 변환됩니다(관리자용).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER400_0: 이미 존재하는 지역입니다.
                            - WEATHER400_2: 올바르지 않은 좌표입니다.
                            - WEATHER400_5: 올바르지 않은 지역코드입니다.
                            """),
            @ApiResponse(responseCode = "403",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER403_0: 접근 권한이 없습니다.
                            - WEATHER403_1: 관리자만 접근할 수 있습니다.
                            """),
            @ApiResponse(responseCode = "404",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER404_0: 지역을 찾을 수 없습니다.
                            """),
            @ApiResponse(responseCode = "500",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER500_1: 격자 좌표 변환 중 오류가 발생했습니다.
                            """)
    })
    public DefaultResponse<RegionResDTO.CreateRegion> createRegion(
            @Valid @RequestBody RegionReqDTO.CreateRegion request) {
        log.info("지역 등록 API 호출: {}", request.name());

        RegionResDTO.CreateRegion response = regionCommandService.createRegion(request);
        return DefaultResponse.ok(response);
    }

    @PostMapping("/with-new-code")
    @Operation(summary = "지역+지역코드 동시 등록 by 김지명", description = "새로운 지역코드와 함께 지역을 등록합니다(관리자용).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER400_0: 이미 존재하는 지역입니다.
                            - WEATHER400_2: 올바르지 않은 좌표입니다.
                            - WEATHER400_5: 올바르지 않은 지역코드입니다.
                            """),
            @ApiResponse(responseCode = "403",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER403_0: 접근 권한이 없습니다.
                            - WEATHER403_1: 관리자만 접근할 수 있습니다.
                            """),
            @ApiResponse(responseCode = "500",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER500_1: 격자 좌표 변환 중 오류가 발생했습니다.
                            """)
    })
    public DefaultResponse<RegionResDTO.CreateRegion> createRegionWithNewCode(
            @Valid @RequestBody RegionReqDTO.CreateRegionWithNewCode request) {
        log.info("지역+지역코드 등록 API 호출: {}", request.name());

        RegionResDTO.CreateRegion response = regionCommandService.createRegionWithNewCode(request);
        return DefaultResponse.ok(response);
    }

    @GetMapping("/codes")
    @Operation(summary = "지역코드 목록 조회 API by 김지명", description = "등록된 모든 지역코드 목록을 조회합니다(관리자용).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "403",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER403_0: 접근 권한이 없습니다.
                            - WEATHER403_1: 관리자만 접근할 수 있습니다.
                            """)
    })
    public DefaultResponse<RegionResDTO.RegionCodeList> getAllRegionCodes() {
        log.info("지역코드 목록 조회 API 호출");

        RegionResDTO.RegionCodeList response = regionQueryService.getAllRegionCodes();
        return DefaultResponse.ok(response);
    }

    @GetMapping
    @Operation(summary = "지역 목록 조회 by 김지명", description = "등록된 모든 지역 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "403",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER403_0: 접근 권한이 없습니다.
                            """)
    })
    public DefaultResponse<RegionResDTO.RegionList> getAllRegions() {
        log.info("지역 목록 조회 API 호출");

        RegionResDTO.RegionList response = regionQueryService.getAllRegions();
        return DefaultResponse.ok(response);
    }

    @GetMapping("/{regionId}")
    @Operation(summary = "지역 상세 조회 by 김지명", description = "특정 지역의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "403",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER403_0: 접근 권한이 없습니다.
                            """),
            @ApiResponse(responseCode = "404",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER404_0: 지역을 찾을 수 없습니다.
                            """)
    })
    public DefaultResponse<RegionResDTO.RegionInfo> getRegion(
            @Parameter(description = "지역 ID", required = true)
            @PathVariable Long regionId) {

        log.info("지역 상세 조회 API 호출: regionId={}", regionId);

        RegionResDTO.RegionInfo response = regionQueryService.getRegionById(regionId);
        return DefaultResponse.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "지역 검색 by 김지명", description = "지역명으로 검색합니다. 부분 일치 검색을 지원합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 성공", useReturnTypeSchema = true)
    })
    public DefaultResponse<RegionResDTO.RegionSearchResult> searchRegions(
            @RequestParam String keyword) {
        RegionResDTO.RegionSearchResult response = regionQueryService.searchRegions(keyword);
        return DefaultResponse.ok(response);
    }

    @DeleteMapping("/codes/{regionCodeId}")
    @Operation(summary = "지역코드 삭제 by 김지명", description = "지역코드를 삭제합니다(해당 코드를 사용하는 지역이 없어야 함. 관리자용).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER400_0: 이미 존재하는 지역입니다.
                            - WEATHER400_5: 올바르지 않은 지역코드입니다.
                            """),
            @ApiResponse(responseCode = "403",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER403_0: 접근 권한이 없습니다.
                            - WEATHER403_1: 관리자만 접근할 수 있습니다.
                            """),
            @ApiResponse(responseCode = "404",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER404_0: 지역을 찾을 수 없습니다.
                            """),
            @ApiResponse(responseCode = "500",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER500_22: 데이터 정리 중 오류가 발생했습니다.
                            """)
    })
    public DefaultResponse<RegionResDTO.DeleteRegionCode> deleteRegionCode(
            @Parameter(description = "지역코드 ID", required = true)
            @PathVariable Long regionCodeId) {
        log.info("지역코드 삭제 API 호출: regionCodeId={}", regionCodeId);

        RegionResDTO.DeleteRegionCode response = regionCommandService.deleteRegionCode(regionCodeId);
        return DefaultResponse.ok(response);
    }

    @DeleteMapping("/{regionId}")
    @Operation(summary = "지역 삭제 by 김지명", description = "지역을 삭제합니다. 연관된 모든 날씨 데이터도 함께 삭제됩니다(관리자용).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER400_0: 이미 존재하는 지역입니다.
                            - WEATHER400_5: 올바르지 않은 지역코드입니다.
                            """),
            @ApiResponse(responseCode = "403",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER403_0: 접근 권한이 없습니다.
                            - WEATHER403_1: 관리자만 접근할 수 있습니다.
                            """),
            @ApiResponse(responseCode = "404",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER404_0: 지역을 찾을 수 없습니다.
                            """),
            @ApiResponse(responseCode = "500",
                    description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - WEATHER500_22: 데이터 정리 중 오류가 발생했습니다.
                            """)
    })
    public DefaultResponse<RegionResDTO.DeleteRegion> deleteRegion(
            @Parameter(description = "지역 ID", required = true)
            @PathVariable Long regionId) {

        log.info("지역 삭제 API 호출: regionId={}", regionId);

        RegionResDTO.DeleteRegion response = regionCommandService.deleteRegion(regionId);

        return DefaultResponse.ok(response);
    }
}
