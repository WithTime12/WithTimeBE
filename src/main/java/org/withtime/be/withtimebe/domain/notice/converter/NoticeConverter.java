package org.withtime.be.withtimebe.domain.notice.converter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.enums.Role;
import org.withtime.be.withtimebe.domain.notice.dto.request.NoticeRequestDTO;
import org.withtime.be.withtimebe.domain.notice.dto.response.NoticeResponseDTO;
import org.withtime.be.withtimebe.domain.notice.entity.Notice;
import org.withtime.be.withtimebe.domain.notice.entity.enums.NoticeCategory;
import org.withtime.be.withtimebe.global.error.code.NoticeErrorCode;
import org.withtime.be.withtimebe.global.error.exception.NoticeException;

public class NoticeConverter {

	// Request DTO : 전체 조회 (Controller -> Service)
 	public static NoticeRequestDTO.FindNoticeList toFindNoticeList(Pageable pageable, String type) {

		NoticeCategory noticeCategory;

		try {
			noticeCategory = NoticeCategory.valueOf(type);
		} catch (IllegalArgumentException e) {
			throw new NoticeException(NoticeErrorCode.NOTICE_CATEGORY_NOT_FOUND);
		}

		return NoticeRequestDTO.FindNoticeList.builder()
			.pageable(pageable)
			.noticeCategory(noticeCategory)
			.build();
	}

	// Request DTO : 검색어 전체 조회 (Controller -> Service)
	public static NoticeRequestDTO.FindNoticeListByKeyword toFindNoticeListByKeyword(Pageable pageable, String keyword, String type) {

		NoticeCategory noticeCategory;

		try {
			noticeCategory = NoticeCategory.valueOf(type);
		} catch (IllegalArgumentException e) {
			throw new NoticeException(NoticeErrorCode.NOTICE_CATEGORY_NOT_FOUND);
		}

		return NoticeRequestDTO.FindNoticeListByKeyword.builder()
			.pageable(pageable)
			.keyword(keyword)
			.noticeCategory(noticeCategory)
			.build();
	}

	// Request : 상세 조회 요청 (Controller -> Service) DTO
	public static NoticeRequestDTO.FindNoticeDetail toFindNoticeDetail(Long noticeId, Member member) {

		return NoticeRequestDTO.FindNoticeDetail.builder()
			.noticeId(noticeId)
			.member(member)
			.build();
	}

	// Response DTO : NoticeResponseDTO.NoticeList
	public static NoticeResponseDTO.NoticeList toNoticeList(Page<Notice> noticePage) {

		List<NoticeResponseDTO.Notice> noticeList = noticePage.getContent().stream()
			.map(NoticeConverter::toNotice)
			.toList();

		return NoticeResponseDTO.NoticeList.builder()
			.noticeList(noticeList)
			.totalPages(noticePage.getTotalPages())
			.currentPage(noticePage.getNumber())
			.currentSize(noticePage.getNumberOfElements())
			.hasNextPage(noticePage.hasNext())
			.build();
	}

	// Response DTO : NoticeResponseDTO.Notice
	public static NoticeResponseDTO.Notice toNotice(Notice notice) {

		return NoticeResponseDTO.Notice.builder()
			.noticeId(notice.getId())
			.title(notice.getTitle())
			.isPinned(notice.getIsPinned())
			.createdAt(notice.getCreatedAt())
			.build();
	}

	// Response : NoticeDetail(DTO)로 변환
	public static NoticeResponseDTO.NoticeDetail toNoticeDetail(Notice notice, Member member) {

		boolean hasAdminAuth = member != null && member.getRole().equals(Role.ADMIN);

		return NoticeResponseDTO.NoticeDetail.builder()
			.noticeId(notice.getId())
			.title(notice.getTitle())
			.content(notice.getContent())
			.isPinned(notice.getIsPinned())
			.hasAdminAuth(hasAdminAuth)
			.createdAt(notice.getCreatedAt())
			.build();
	}
}
