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
	public static NoticeResponseDTO.NoticeDetail toNoticeDetail(Notice notice) {

		return NoticeResponseDTO.NoticeDetail.builder()
			.noticeId(notice.getId())
			.title(notice.getTitle())
			.content(notice.getContent())
			.isPinned(notice.getIsPinned())
			.createdAt(notice.getCreatedAt())
			.build();
	}

	public static Notice toNoticeEntity(NoticeRequestDTO.CreateNotice request, Member member) {

		return Notice.builder()
			.member(member)
			.title(request.title())
			.content(request.content())
			.isPinned(request.isPinned())
			.noticeCategory(request.noticeCategory())
			.build();
	}
}
