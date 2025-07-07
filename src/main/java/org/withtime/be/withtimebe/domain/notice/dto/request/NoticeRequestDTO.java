package org.withtime.be.withtimebe.domain.notice.dto.request;

import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.notice.entity.enums.NoticeCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record NoticeRequestDTO() {

	@Builder
	public record FindNoticeList(
		Pageable pageable,    // 게시글 식별자 값
		NoticeCategory noticeCategory	// 게시글 유형
	) {}

	@Builder
	public record FindNoticeListByKeyword(
		Pageable pageable,    // 게시글 식별자 값
		String keyword,		// 검색 키워드
		NoticeCategory noticeCategory	// 게시글 유형
	) {}

	@Builder
	public record FindNoticeDetail(
		Long noticeId,
		Member member
	) {}

	public record CreateNotice(
		@NotBlank(message = "제목을 입력해주세요")
		String title,
		@NotBlank(message = "내용을 입력해주세요")
		String content,
		@NotNull(message = "상단 고정 여부를 결정해주세요")
		Boolean isPinned
	) {}
}
