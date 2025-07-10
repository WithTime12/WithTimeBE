package org.withtime.be.withtimebe.domain.faq.dto.request;

import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.faq.entity.enums.FaqCategory;

import lombok.Builder;

public class FaqRequestDTO {

	@Builder
	public record FindFaqList(
		Pageable pageable,    // 자주 묻는 질문글 식별자 값
		FaqCategory faqCategory    // 질문 유형
	) {}

	@Builder
	public record FindFaqListByKeyword(
		Pageable pageable,    // 자주 묻는 질문글 식별자 값
		String keyword,		// 검색 키워드
		FaqCategory faqCategory	// 질문 유형
	) {}
}
