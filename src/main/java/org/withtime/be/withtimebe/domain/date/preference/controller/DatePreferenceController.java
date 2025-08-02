package org.withtime.be.withtimebe.domain.date.preference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.*;
import org.withtime.be.withtimebe.domain.date.preference.converter.DatePreferenceConverter;
import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceRequestDTO;
import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceResponseDTO;
import org.withtime.be.withtimebe.domain.date.preference.service.command.DatePreferenceTestCommandService;
import org.withtime.be.withtimebe.domain.date.preference.service.query.DatePreferenceDescriptionQueryService;
import org.withtime.be.withtimebe.domain.date.preference.service.query.DatePreferenceQuestionQueryService;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.global.security.annotation.AuthenticatedMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dates/preferences")
@Tag(name = "데이트 취향 테스트 API")
public class DatePreferenceController {

    private final DatePreferenceTestCommandService datePreferenceTestCommandService;
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

    @Operation(summary = "데이트 취향 테스트 API", description = "질문에 대한 답변으로 결과 생성하는 API 1, 2 둘 중 하나로 40개로 채워 배열 형태로 전송")
    @PostMapping("/tests")
    public DefaultResponse<DatePreferenceResponseDTO.TestResult> test(@AuthenticatedMember Member member, @RequestBody DatePreferenceRequestDTO.Test request) {
        return DefaultResponse.ok(datePreferenceTestCommandService.test(member, request));
    }

}
