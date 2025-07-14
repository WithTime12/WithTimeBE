package org.withtime.be.withtimebe.domain.member.controller.query;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.member.converter.MemberConverter;
import org.withtime.be.withtimebe.domain.member.dto.response.MemberResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.service.MemberQueryService;
import org.withtime.be.withtimebe.global.annotation.SwaggerPageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberQueryController {

	private final MemberQueryService memberQueryService;

	@Operation(summary = "멤버십 목록 전체 조회 API by 피우", description = "멤버십 목록 전체 조회 API입니다. (검색어 X)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다.")
	})
	@SwaggerPageable
	@GetMapping
	public DefaultResponse<MemberResponseDTO.MemberList> findMemberList(@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Page<Member> result = memberQueryService.findMemberList(pageable);
		MemberResponseDTO.MemberList response = MemberConverter.toMemberList(result);
		return DefaultResponse.ok(response);
	}

}
