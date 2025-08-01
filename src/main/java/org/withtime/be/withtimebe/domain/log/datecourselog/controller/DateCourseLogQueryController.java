package org.withtime.be.withtimebe.domain.log.datecourselog.controller;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.date.repository.DateCourseRepository;
import org.withtime.be.withtimebe.domain.log.datecourselog.dto.DateCourseLogResponseDTO;
import org.withtime.be.withtimebe.domain.log.datecourselog.service.query.DateCourseLogQueryService;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.global.security.annotation.AuthenticatedMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs/datecourses")
public class DateCourseLogQueryController {

	private final DateCourseLogQueryService dateCourseLogQueryService;

	@Operation(summary = "최근 1개월 WithTime 사용자의 데이트 평균 횟수와 나의 데이트 횟수 조회 API by 피우", description = "메인 페이지의 데이트 나침반에 해당하는 API입니다. 최근 1개월 WithTime 사용자의 데이트 평균 횟수와 나의 데이트 횟수 조회하는 API입니다. 비회원은 나의 데이트 횟수가 0회로 표시됩니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다.")
	})
	@GetMapping("/average")
	public DefaultResponse<DateCourseLogResponseDTO.FindAverageDateCourseCount> findAverageDateCourseCount(
		@AuthenticatedMember Member member
	) {
		DateCourseLogResponseDTO.FindAverageDateCourseCount response = dateCourseLogQueryService.findAverageDateCourseCount(member);
		return DefaultResponse.ok(response);
	}

	@Operation(summary = "다른 사람의 내 데이트 코스 저장 횟수 조회 API by 피우", description = "나의 데이트 코스를 다른 사람이 얼마나 저장했는지 조회하는 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다.")
	})
	@GetMapping("/saved-count")
	public DefaultResponse<DateCourseLogResponseDTO.FindSavedDateCourseCount> findSavedDateCourseCount(
		@AuthenticatedMember Member member
	) {
		DateCourseLogResponseDTO.FindSavedDateCourseCount response = dateCourseLogQueryService.findSavedDateCourseCount(member);
		return DefaultResponse.ok(response);
	}
}
