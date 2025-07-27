package org.withtime.be.withtimebe.domain.log.placecategorylog.controller;

import java.util.List;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.log.placecategorylog.converter.PlaceCategoryLogConverter;
import org.withtime.be.withtimebe.domain.log.placecategorylog.dto.PlaceCategoryLogResponseDTO;
import org.withtime.be.withtimebe.domain.log.placecategorylog.model.PlaceCategoryLog;
import org.withtime.be.withtimebe.domain.log.placecategorylog.service.query.PlaceCategoryLogQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/logs/keyword")
public class PlaceCategoryLogQueryController {

	private final PlaceCategoryLogQueryService placeCategoryLogQueryService;

	@Operation(summary = "이번 주 인기 키워드 조회 API by 피우", description = "이번 주 많이 찾은 키워드를 조회하는 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다.")
	})
	@GetMapping("/weekly")
	public DefaultResponse<PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLogList> findWeeklyPlaceCategoryLogList() {
		List<PlaceCategoryLog> result = placeCategoryLogQueryService.findWeeklyPlaceCategoryLogList();
		PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLogList response = PlaceCategoryLogConverter.toWeeklyPlaceCategoryLogList(result);
		return DefaultResponse.ok(response);
	}
}
