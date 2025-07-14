package org.withtime.be.withtimebe.domain.notice.controller.query;


import org.namul.api.payload.response.DefaultResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.notice.converter.NoticeConverter;
import org.withtime.be.withtimebe.domain.notice.dto.request.NoticeRequestDTO;
import org.withtime.be.withtimebe.domain.notice.dto.response.NoticeResponseDTO;
import org.withtime.be.withtimebe.domain.notice.entity.Notice;
import org.withtime.be.withtimebe.domain.notice.service.query.NoticeQueryService;
import org.withtime.be.withtimebe.global.annotation.SwaggerPageable;
import org.withtime.be.withtimebe.global.security.annotation.AuthenticatedMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notices")
public class NoticeQueryController {

	private final NoticeQueryService noticeQueryService;

	@Operation(summary = "공지사항 전체 조회 API by 피우", description = "공지사항 전체 조회 API입니다. (검색어 X)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(
			responseCode = "404",
			description = """
				- NOTICE404_1 : 해당하는 공지사항 유형을 찾을 수 없습니다."
			""")
	})
	@Parameter(name = "noticeCategory", description = "SYSTEM / SERVICE")
	@SwaggerPageable
	@GetMapping
	public DefaultResponse<NoticeResponseDTO.NoticeList> findNoticeList(
		@PageableDefault(page = 0, size = 10) Pageable pageable,
		@RequestParam String noticeCategory
	) {
		NoticeRequestDTO.FindNoticeList request = NoticeConverter.toFindNoticeList(pageable, noticeCategory);
		Page<Notice> result = noticeQueryService.findNoticeList(request);
		NoticeResponseDTO.NoticeList response = NoticeConverter.toNoticeList(result);
		return DefaultResponse.ok(response);
	}

	@Operation(summary = "공지사항 검색어 전체 조회 API by 피우", description = "공지사항 전체 조회 API입니다. (검색어 O)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(
			responseCode = "404",
			description = """
				- NOTICE404_1 : 해당하는 공지사항 유형을 찾을 수 없습니다."
			""")
	})
	@Parameter(name = "noticeCategory", description = "SYSTEM / SERVICE")
	@SwaggerPageable
	@GetMapping("/search")
	public DefaultResponse<NoticeResponseDTO.NoticeList> findNoticeListByKeyword(
		@PageableDefault(page = 0, size = 10) Pageable pageable,
		@RequestParam String keyword,
		@RequestParam String noticeCategory
	) {
		NoticeRequestDTO.FindNoticeListByKeyword request = NoticeConverter.toFindNoticeListByKeyword(pageable, keyword, noticeCategory);
		Page<Notice> result = noticeQueryService.findNoticeListByKeyword(request);
		NoticeResponseDTO.NoticeList response = NoticeConverter.toNoticeList(result);
		return DefaultResponse.ok(response);
	}

	@Operation(summary = "공지사항 상세 조회 API by 피우", description = "공지사항 상세 조회 API입니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(
			responseCode = "403",
			description = """
				- NOTICE403_1 : 삭제된 공지사항을 열람할 Admin 권한이 없습니다.
			"""),
		@ApiResponse(
			responseCode = "404",
			description = """
				- NOTICE404_2 : 해당하는 공지사항을 찾을 수 없습니다.
			""")
	})
	@GetMapping("/{noticeId}")
	public DefaultResponse<NoticeResponseDTO.NoticeDetail> findNoticeDetail(
		@PathVariable("noticeId") Long noticeId,
		@AuthenticatedMember Member member
	) {
		NoticeRequestDTO.FindNoticeDetail request = NoticeConverter.toFindNoticeDetail(noticeId, member);
		Notice result = noticeQueryService.findNoticeDetail(request);
		NoticeResponseDTO.NoticeDetail response = NoticeConverter.toNoticeDetail(result, member);
		return DefaultResponse.ok(response);
	}

	@Operation(summary = "삭제된 공지사항 전체 조회 API by 피우 [Only Admin]", description = "삭제된 공지사항 상세 조회 API입니다. 어드민만 사용 가능합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공입니다."),
		@ApiResponse(responseCode = "403",
			description = """
				- COMMON403 : "Admin 권한이 없음을 의미합니다."
			"""),
		@ApiResponse(
			responseCode = "404",
			description = """
				- NOTICE404_1 : "해당하는 공지사항 유형을 찾을 수 없습니다."
			""")
	})
	@Parameter(name = "noticeCategory", description = "SYSTEM / SERVICE")
	@SwaggerPageable
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/trash")
	public DefaultResponse<NoticeResponseDTO.NoticeList> findTrashNoticeList(
		@PageableDefault(page = 0, size = 10) Pageable pageable,
		@RequestParam String noticeCategory
	) {
		NoticeRequestDTO.FindNoticeList request = NoticeConverter.toFindNoticeList(pageable, noticeCategory);
		Page<Notice> result = noticeQueryService.findTrashNoticeList(request);
		NoticeResponseDTO.NoticeList response = NoticeConverter.toNoticeList(result);
		return DefaultResponse.ok(response);
	}
}
