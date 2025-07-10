package org.withtime.be.withtimebe.domain.faq.controller;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.faq.converter.FaqConverter;
import org.withtime.be.withtimebe.domain.faq.dto.request.FaqRequestDTO;
import org.withtime.be.withtimebe.domain.faq.dto.response.FaqResponseDTO;
import org.withtime.be.withtimebe.domain.faq.entity.Faq;
import org.withtime.be.withtimebe.domain.faq.service.command.FaqCommandService;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.global.security.annotation.AuthenticatedMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class FaqCommandController {

	private final FaqCommandService faqCommandService;

	@Operation(summary = "자주 묻는 질문 생성 API Only Admin by 피우", description = "자주 묻는 질문 생성 API입니다. 어드민만 사용 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(responseCode = "403",
			description = """
				- COMMON403 : "Admin 권한이 없음을 의미합니다."
			""")
	})
	@PostMapping("/faqs")
	public DefaultResponse<FaqResponseDTO.Faq> createFaq(
		@RequestBody @Valid FaqRequestDTO.CreateFaq request,
		@AuthenticatedMember Member member
	) {
		Faq result = faqCommandService.createFaq(request, member);
		FaqResponseDTO.Faq response = FaqConverter.toFaq(result);
		return DefaultResponse.created(response);
	}
}
