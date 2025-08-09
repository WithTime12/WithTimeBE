package org.withtime.be.withtimebe.domain.member.controller;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.member.converter.MemberConverter;
import org.withtime.be.withtimebe.domain.member.dto.GradeResponseDTO;
import org.withtime.be.withtimebe.domain.member.dto.MemberResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.service.query.GradeQueryService;
import org.withtime.be.withtimebe.global.security.annotation.AuthenticatedMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members/grade")
@Tag(name = "사용자 등급 관련 API")
public class GradeController {

	private final GradeQueryService gradeQueryService;

	@Operation(summary = "나의 등급 조회 API by 피우", description = "나의 등급을 조회하는 API 입니다. 로그인한 사용자만 조회 가능합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "등급 반환 성공"),
		@ApiResponse(
			responseCode = "404",
			description = """
                            다음과 같은 이유로 실패할 수 있습니다:
                            - GRADE404_1: 등급을 찾지 못했습니다.
                          """
		)
	})
	@GetMapping
	public DefaultResponse<GradeResponseDTO.FindMyGrade> findMyGrade(@AuthenticatedMember Member member) {
		GradeResponseDTO.FindMyGrade response = gradeQueryService.findMyGrade(member);
		return DefaultResponse.ok(response);
	}
}
