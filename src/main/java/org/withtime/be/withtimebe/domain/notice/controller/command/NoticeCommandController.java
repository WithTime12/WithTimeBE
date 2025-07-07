package org.withtime.be.withtimebe.domain.notice.controller.command;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.notice.converter.NoticeConverter;
import org.withtime.be.withtimebe.domain.notice.dto.request.NoticeRequestDTO;
import org.withtime.be.withtimebe.domain.notice.dto.response.NoticeResponseDTO;
import org.withtime.be.withtimebe.domain.notice.entity.Notice;
import org.withtime.be.withtimebe.domain.notice.service.command.NoticeCommandService;
import org.withtime.be.withtimebe.global.security.annotation.AuthenticatedMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/notices")
public class NoticeCommandController {

	private final NoticeCommandService noticeCommandService;

	@Operation(summary = "공지사항 생성 API Only Admin by 피우", description = "공지사항 생성 API입니다. 어드민만 사용 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(responseCode = "403",
			description = """
				- COMMON403 : "Admin 권한이 없음을 의미합니다."
			""")
	})
	@PostMapping
	public DefaultResponse<NoticeResponseDTO.Notice> createNotice(
		@RequestBody @Valid NoticeRequestDTO.CreateNotice request,
		@AuthenticatedMember Member member
	) {
		Notice result = noticeCommandService.createNotice(request, member);
		NoticeResponseDTO.Notice response = NoticeConverter.toNotice(result);
		return DefaultResponse.created(response);
	}
}
