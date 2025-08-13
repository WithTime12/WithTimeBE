package org.withtime.be.withtimebe.domain.faq.controller;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/faqs")
@Tag(name = "자주 묻는 질문 수정 관련 API")
public class FaqCommandController {

	private final FaqCommandService faqCommandService;

	@Operation(summary = "자주 묻는 질문 생성 API by 피우 [Only Admin]", description = "자주 묻는 질문 생성 API입니다. 어드민만 사용 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(responseCode = "403",
			description = """
				- COMMON403 : "Admin 권한이 없음을 의미합니다."
			""")
	})
	@PostMapping
	public DefaultResponse<FaqResponseDTO.Faq> createFaq(
		@RequestBody @Valid FaqRequestDTO.CreateFaq request,
		@AuthenticatedMember Member member
	) {
		Faq result = faqCommandService.createFaq(request, member);
		FaqResponseDTO.Faq response = FaqConverter.toFaq(result);
		return DefaultResponse.created(response);
	}

	@Operation(summary = "자주 묻는 질문 수정 API by 피우 [Only Admin]", description = "자주 묻는 질문 수정 API입니다. 어드민만 사용 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(responseCode = "403",
			description = """
				- COMMON403 : "Admin 권한이 없음을 의미합니다."
			"""),
		@ApiResponse(responseCode = "404",
			description = """
				- FAQ404_2 : "해당하는 질문글을 찾을 수 없습니다."
			""")
	})
	@PutMapping("/{faqId}")
	public DefaultResponse<FaqResponseDTO.Faq> updateFaq(
		@PathVariable("faqId") Long faqId,
		@RequestBody @Valid FaqRequestDTO.UpdateFaq request
	) {
		Faq result = faqCommandService.updateFaq(request, faqId);
		FaqResponseDTO.Faq response = FaqConverter.toFaq(result);
		return DefaultResponse.ok(response);
	}

	@Operation(summary = "자주 묻는 질문 삭제 API by 피우 [Only Admin]", description = "자주 묻는 질문 삭제 API입니다. 어드민만 사용 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(responseCode = "403",
			description = """
				- COMMON403 : "Admin 권한이 없음을 의미합니다."
			"""),
		@ApiResponse(responseCode = "404",
			description = """
				- FAQ404_2 : "해당하는 질문글을 찾을 수 없습니다."
			""")
	})
	@DeleteMapping("/{faqId}")
	public DefaultResponse<String> deleteFaq(@PathVariable("faqId") Long faqId) {
		faqCommandService.deleteFaq(faqId);
		return DefaultResponse.noContent();
	}
}
