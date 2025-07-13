package org.withtime.be.withtimebe.domain.faq.dto.request;

import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.faq.entity.enums.FaqCategory;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class FaqRequestDTO {

	public record CreateFaq(
		@NotBlank(message = "제목을 입력해주세요")
		String title,
		@NotBlank(message = "내용을 입력해주세요")
		String content,
		@NotNull(message = "질문 유형을 입력해주세요")
		FaqCategory faqCategory
	) {}

	public record UpdateFaq (
		@NotBlank(message = "제목을 입력해주세요")
		String title,
		@NotBlank(message = "내용을 입력해주세요")
		String content
	) {}
}
