package org.withtime.be.withtimebe.domain.notice.controller.command;

import org.namul.api.payload.response.DefaultResponse;
import org.springframework.security.access.prepost.PreAuthorize;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/notices")
@Tag(name = "공지사항 수정 관련 API")
public class NoticeCommandController {

	private final NoticeCommandService noticeCommandService;

	@Operation(summary = "공지사항 생성 API by 피우 [Only Admin]", description = "공지사항 생성 API입니다. 어드민만 사용 가능합니다.")
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

	@Operation(summary = "공지사항 수정 API by 피우 [Only Admin]", description = "공지사항 수정 API입니다. 어드민만 사용 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(responseCode = "403",
			description = """
				- COMMON403 : "Admin 권한이 없음을 의미합니다."
			"""),
		@ApiResponse(responseCode = "404",
			description = """
				- NOTICE404_2 : "해당하는 공지사항을 찾을 수 없습니다."
			""")
	})
	@PutMapping("/{noticeId}")
	public DefaultResponse<NoticeResponseDTO.Notice> updateNotice(
		@PathVariable Long noticeId,
		@RequestBody @Valid NoticeRequestDTO.UpdateNotice request
	) {
		Notice result = noticeCommandService.updateNotice(request, noticeId);
		NoticeResponseDTO.Notice response = NoticeConverter.toNotice(result);
		return DefaultResponse.ok(response);
	}

	@Operation(summary = "공지사항 삭제 API by 피우 [Only Admin]", description = "공지사항 삭제 API입니다. 어드민만 사용 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "성공입니다."),
		@ApiResponse(responseCode = "403",
			description = """
				- COMMON403 : "Admin 권한이 없음을 의미합니다."
			"""),
		@ApiResponse(responseCode = "404",
			description = """
				- NOTICE404_2 : "해당하는 공지사항을 찾을 수 없습니다."
			""")
	})
	@DeleteMapping("/{noticeId}")
	public DefaultResponse<String> softDeleteNotice(@PathVariable Long noticeId) {
		noticeCommandService.softDeleteNotice(noticeId);
		return DefaultResponse.noContent();
	}

	@Operation(summary = "삭제한 공지사항 되돌리기 API by 피우 [Only Admin]", description = "삭제한 공지사항을 되돌리는 API입니다. 어드민만 사용 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(responseCode = "403",
			description = """
				- COMMON403 : "Admin 권한이 없음을 의미합니다."
			"""),
		@ApiResponse(responseCode = "404",
			description = """
				- NOTICE404_2 : "해당하는 공지사항을 찾을 수 없습니다."
			""")
	})
	@PatchMapping("/{noticeId}")
	public DefaultResponse<NoticeResponseDTO.Notice> recoverDeletedNotice(@PathVariable Long noticeId) {
		Notice result = noticeCommandService.recoverDeletedNotice(noticeId);
		NoticeResponseDTO.Notice response = NoticeConverter.toNotice(result);
		return DefaultResponse.ok(response);
	}
}
