package org.withtime.be.withtimebe.domain.log.controller.query;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.log.converter.VisitLogConverter;
import org.withtime.be.withtimebe.domain.log.dto.response.VisitLogResponseDTO;
import org.withtime.be.withtimebe.domain.log.entity.VisitLog;
import org.withtime.be.withtimebe.domain.log.service.query.VisitLogQueryService;
import org.withtime.be.withtimebe.global.annotation.SwaggerPageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/visit-logs")
public class VisitLogQueryController {

	private final VisitLogQueryService visitLogQueryService;

	@Operation(summary = "최근 일주일 간 일별 방문자 수 조회 API Only Admin by 피우", description = "일주일 간 일별 방문자 수 조회 API입니다. 어드민만 사용 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다.")
	})
	@GetMapping("/daily")
	public DefaultResponse<VisitLogResponseDTO.DailyVisitLogList> findDailyVisitLogList() {
		List<VisitLog> result = visitLogQueryService.findDailyVisitLogList();
		VisitLogResponseDTO.DailyVisitLogList response = VisitLogConverter.toDailyVisitLogList(result);
		return DefaultResponse.ok(response);
	}
}
