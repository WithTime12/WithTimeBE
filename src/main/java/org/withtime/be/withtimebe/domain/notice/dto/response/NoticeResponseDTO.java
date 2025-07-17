package org.withtime.be.withtimebe.domain.notice.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

public record NoticeResponseDTO() {

	@Builder
	public record NoticeList(
		List<Notice> noticeList,
		Integer totalPages,	// 전체 페이지 개수
		Integer currentPage,	// 현재 페이지 번호
		Integer currentSize,	// 현재 페이지의 크기
		Boolean hasNextPage	// 다음 페이지 존재 여부
	) {}

	@Builder
	public record Notice(
		Long noticeId,	// 게시글 식별자 값
		String title,	// 게시글 제목
		Boolean isPinned,	// 고정 여부
		LocalDateTime createdAt	// 생성 날짜
	) {}

	@Builder
	public record NoticeDetail(
		Long noticeId,    // 게시글 식별자 값
		String title,    // 게시글 제목
		String content,    // 게시글 내용
		Boolean isPinned,    // 고정 여부
		LocalDateTime createdAt    // 생성 날짜
	) {}
}
