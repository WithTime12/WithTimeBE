package org.withtime.be.withtimebe.domain.date.preference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.date.preference.converter.DatePreferenceConverter;
import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceResponseDTO;
import org.withtime.be.withtimebe.domain.date.preference.service.query.DatePreferenceDescriptionQueryService;
import org.withtime.be.withtimebe.domain.date.preference.service.query.DatePreferenceQuestionQueryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dates/preferences")
@Tag(name = "데이트 취향 테스트 API")
public class DatePreferenceController {

    private final DatePreferenceDescriptionQueryService datePreferenceDescriptionQueryService;
    private final DatePreferenceQuestionQueryService datePreferenceQuestionQueryService;

    @Operation(summary = "데이트 취향 조회 API", description = "데이트 취향 종류 조회")
    @ApiResponse(responseCode = "COMMON200", description = "성공적으로 정보를 가져왔습니다.")
    @GetMapping
    public DefaultResponse<DatePreferenceResponseDTO.FindTypes> findTypes() {
        return DefaultResponse.ok(DatePreferenceConverter.toFindTypes(datePreferenceDescriptionQueryService.findTypes()));
    }

    @Operation(summary = "데이트 취향 테스트 질문 조회 API", description = "데이트 취향 테스트에 사용되는 질문 조회하는 API")
    @ApiResponse(responseCode = "COMMON200", description = "성공적으로 정보를 가져왔습니다.")
    @GetMapping("/questions")
    public DefaultResponse<DatePreferenceResponseDTO.FindQuestions> findQuestions() {
        return DefaultResponse.ok(DatePreferenceConverter.toFindQuestions(datePreferenceQuestionQueryService.findQuestions()));
    }
}
