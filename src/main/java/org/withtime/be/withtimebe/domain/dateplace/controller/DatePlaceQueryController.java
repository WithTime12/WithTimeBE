package org.withtime.be.withtimebe.domain.dateplace.controller;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.dateplace.converter.DatePlaceConverter;
import org.withtime.be.withtimebe.domain.dateplace.dto.response.DatePlaceResponseDTO;
import org.withtime.be.withtimebe.domain.dateplace.entity.DatePlace;
import org.withtime.be.withtimebe.domain.dateplace.service.query.DatePlaceQueryService;
import org.withtime.be.withtimebe.global.annotation.SwaggerPageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dateplaces")
public class DatePlaceQueryController {

	private final DatePlaceQueryService datePlaceQueryService;

	@Operation(summary = "어드민 페이지 데이트 장소 조회 API by 피우 [Only Admin]", description = "어드민 페이지에서 데이트 장소를 조회하는 API입니다. 어드민만 사용 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다.")
	})
	@SwaggerPageable
	@GetMapping("/management")
	public DefaultResponse<DatePlaceResponseDTO.DatePlaceManagementList> findDatePlaceManagement(
		@PageableDefault(page = 0, size = 10) Pageable pageable
	) {
		Page<DatePlace> result = datePlaceQueryService.findDatePlaces(pageable);
		DatePlaceResponseDTO.DatePlaceManagementList response = DatePlaceConverter.toDatePlaceManagementList(result);
		return DefaultResponse.ok(response);
	}
}
