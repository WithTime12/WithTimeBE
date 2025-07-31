package org.withtime.be.withtimebe.domain.log.dateplacelog.controller;

import java.util.List;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.log.dateplacelog.converter.DatePlaceLogConverter;
import org.withtime.be.withtimebe.domain.log.dateplacelog.dto.DatePlaceLogResponseDTO;
import org.withtime.be.withtimebe.domain.log.dateplacelog.entity.DatePlaceLog;
import org.withtime.be.withtimebe.domain.log.dateplacelog.service.query.DatePlaceLogQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs/dateplaces")
@Tag(name = "데이트 장소 통계 관련 API")
public class DatePlaceLogQueryController {

	private final DatePlaceLogQueryService datePlaceLogQueryService;

	@Operation(summary = "WithTime에 등록된 월별 데이트 장소 수 조회 API by 피우", description = "WithTime에 등록된 월별 데이트 장소를 조회하는 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다.")
	})
	@GetMapping("/monthly")
	public DefaultResponse<DatePlaceLogResponseDTO.MonthlyDatePlaceLogList> findMonthlyDatePlaceLogList() {
		List<DatePlaceLog> result = datePlaceLogQueryService.findMonthlyDatePlaceLogList();
		DatePlaceLogResponseDTO.MonthlyDatePlaceLogList response = DatePlaceLogConverter.toMonthlyDatePlaceLogList(result);
		return DefaultResponse.ok(response);
	}
}