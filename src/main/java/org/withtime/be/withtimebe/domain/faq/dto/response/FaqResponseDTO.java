package org.withtime.be.withtimebe.domain.faq.dto.response;

import java.util.List;

import lombok.Builder;

public class FaqResponseDTO {

	@Builder
	public record FaqList(
		List<Faq> faqList,
		Integer totalPages,	// 전체 페이지 개수
		Integer currentPage,	// 현재 페이지 번호
		Integer currentSize,	// 현재 페이지의 크기
		Boolean hasNextPage	// 다음 페이지 존재 여부
	) {}

	@Builder
	public record Faq(
		Long faqId,    // 자주 묻는 질문글 식별자 값
		String title,    // 질문글 제목
		String content		// 내용
	) {}
}
