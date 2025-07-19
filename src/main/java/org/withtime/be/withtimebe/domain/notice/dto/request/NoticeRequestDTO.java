package org.withtime.be.withtimebe.domain.notice.dto.request;

import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.notice.entity.enums.NoticeCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record NoticeRequestDTO() {

	public record CreateNotice(
		@NotBlank(message = "제목을 입력해주세요")
		String title,
		@NotBlank(message = "내용을 입력해주세요")
		String content,
		@NotNull(message = "상단 고정 여부를 결정해주세요")
		Boolean isPinned,
		@NotNull(message = "공지사항 유형을 입력해주세요")
		NoticeCategory noticeCategory
	) {}

	public record UpdateNotice (
		@NotBlank(message = "제목을 입력해주세요")
		String title,
		@NotBlank(message = "내용을 입력해주세요")
		String content,
		@NotNull(message = "상단 고정 여부를 결정해주세요")
		Boolean isPinned
	) {}
}
