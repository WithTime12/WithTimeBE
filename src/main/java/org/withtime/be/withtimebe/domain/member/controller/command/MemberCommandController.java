package org.withtime.be.withtimebe.domain.member.controller.command;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.member.converter.MemberConverter;
import org.withtime.be.withtimebe.domain.member.dto.request.MemberRequestDTO;
import org.withtime.be.withtimebe.domain.member.dto.response.MemberResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.service.command.MemberCommandService;
import org.withtime.be.withtimebe.global.annotation.SwaggerPageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberCommandController {

	private final MemberCommandService memberCommandService;

	@Operation(summary = "어드민 멤버십 관리하기 API by 피우 [Only Admin]", description = "어드민 창의 멤버십 수정하기 API입니다. 어드민만 사용 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(responseCode = "400",
			description = """
				- MEMBER404_1 : 사용자를 찾지 못했습니다.
				- MEMBER404_2 : 멤버십을 찾지 못했습니다.
			""")
	})
	@SwaggerPageable
	@PutMapping("/{memberId}/membership")
	public DefaultResponse<MemberResponseDTO.Membership> updateMembership(
		@PathVariable Long memberId,
		@RequestBody @Valid MemberRequestDTO.UpdateMembership request
	) {
		Member result = memberCommandService.updateMembership(request, memberId);
		MemberResponseDTO.Membership response = MemberConverter.toMembership(result);
		return DefaultResponse.ok(response);
	}
}
