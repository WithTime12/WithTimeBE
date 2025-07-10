package org.withtime.be.withtimebe.domain.faq.dto.request;

import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.*;

import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.faq.entity.enums.FaqCategory;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
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
		String keyword,        // 검색 키워드
		FaqCategory faqCategory    // 질문 유형
	) {}

	public record CreateFaq(
		@NotBlank(message = "제목을 입력해주세요")
		String title,
		@NotBlank(message = "내용을 입력해주세요")
		String content,
		@NotNull(message = "질문글 유형을 입력해주세요")
		FaqCategory faqCategory
	) {}

	public record UpdateFaq (
		@NotBlank(message = "제목을 입력해주세요")
		String title,
		@NotBlank(message = "내용을 입력해주세요")
		String content
	) {}

}
