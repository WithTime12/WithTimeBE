package org.withtime.be.withtimebe.domain.faq.controller;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.faq.converter.FaqConverter;
import org.withtime.be.withtimebe.domain.faq.dto.request.FaqRequestDTO;
import org.withtime.be.withtimebe.domain.faq.dto.response.FaqResponseDTO;
import org.withtime.be.withtimebe.domain.faq.entity.Faq;
import org.withtime.be.withtimebe.domain.faq.service.query.FaqQueryService;
import org.withtime.be.withtimebe.global.annotation.SwaggerPageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class FaqQueryController {

	private final FaqQueryService faqQueryService;

	@Operation(summary = "자주 묻는 질문 전체 조회 API by 피우", description = "자주 묻는 질문 전체 조회 API입니다. (검색어 X)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(
			responseCode = "404",
			description = """
				- FAQ404_1 : 해당하는 질문 유형을 찾을 수 없습니다.
			""")
	})
	@Parameter(name = "faqCategory", description = "USAGE / ALGORITHM / FEATURE / SCHEDULE / ERROR / ACCOUNT")
	@SwaggerPageable
	@GetMapping("/faqs")
	public DefaultResponse<FaqResponseDTO.FaqList> findFaqList(
		@PageableDefault(page = 0, size = 10) Pageable pageable,
		@RequestParam String faqCategory
	) {
		FaqRequestDTO.FindFaqList request = FaqConverter.toFindFaqList(pageable, faqCategory);
		Page<Faq> result = faqQueryService.findFaqList(request);
		FaqResponseDTO.FaqList response = FaqConverter.toFaqList(result);
		return DefaultResponse.ok(response);
	}

	@Operation(summary = "자주 묻는 질문 검색어 전체 조회 API by 피우", description = "자주 묻는 질문 검색어 전체 조회 API입니다. (검색어 O)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(
			responseCode = "404",
			description = """
				- FAQ404_1 : 해당하는 질문 유형을 찾을 수 없습니다.
			""")
	})
	@Parameter(name = "faqCategory", description = "USAGE / ALGORITHM / FEATURE / SCHEDULE / ERROR / ACCOUNT")
	@SwaggerPageable
	@GetMapping("/faqs/search")
	public DefaultResponse<FaqResponseDTO.FaqList> findFaqListByKeyword(
		@PageableDefault(page = 0, size = 10) Pageable pageable,
		@RequestParam String keyword,
		@RequestParam String faqCategory
	) {
		FaqRequestDTO.FindFaqListByKeyword request = FaqConverter.toFindFaqListByKeyword(pageable, keyword, faqCategory);
		Page<Faq> result = faqQueryService.findFaqListByKeyword(request);
		FaqResponseDTO.FaqList response = FaqConverter.toFaqList(result);
		return DefaultResponse.ok(response);
	}
}
