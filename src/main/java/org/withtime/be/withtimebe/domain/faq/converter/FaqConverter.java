package org.withtime.be.withtimebe.domain.faq.converter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.faq.dto.request.FaqRequestDTO;
import org.withtime.be.withtimebe.domain.faq.dto.response.FaqResponseDTO;
import org.withtime.be.withtimebe.domain.faq.entity.Faq;
import org.withtime.be.withtimebe.domain.faq.entity.enums.FaqCategory;
import org.withtime.be.withtimebe.global.error.code.FaqErrorCode;
import org.withtime.be.withtimebe.global.error.exception.FaqException;

public class FaqConverter {

	// Request DTO : 전체 조회 (Controller -> Service)
	public static FaqRequestDTO.FindFaqList toFindFaqList(Pageable pageable, String type) {

		FaqCategory faqCategory;

		try {
			faqCategory = FaqCategory.valueOf(type);
		} catch (IllegalArgumentException e) {
			throw new FaqException(FaqErrorCode.FAQ_CATEGORY_NOT_FOUND);
		}

		return FaqRequestDTO.FindFaqList.builder()
			.pageable(pageable)
			.faqCategory(faqCategory)
			.build();
	}

	// Request DTO : 검색어 전체 조회 (Controller -> Service)
	public static FaqRequestDTO.FindFaqListByKeyword toFindFaqListByKeyword(Pageable pageable, String keyword, String type) {

		FaqCategory faqCategory;

		try {
			faqCategory = FaqCategory.valueOf(type);
		} catch (IllegalArgumentException e) {
			throw new FaqException(FaqErrorCode.FAQ_CATEGORY_NOT_FOUND);
		}

		return FaqRequestDTO.FindFaqListByKeyword.builder()
			.pageable(pageable)
			.keyword(keyword)
			.faqCategory(faqCategory)
			.build();
	}

	// Response DTO : FaqResponseDTO.FaqList
	public static FaqResponseDTO.FaqList toFaqList(Page<Faq> faqPage) {

		List<FaqResponseDTO.Faq> faqList = faqPage.getContent().stream()
			.map(FaqConverter::toFaq)
			.toList();

		return FaqResponseDTO.FaqList.builder()
			.faqList(faqList)
			.totalPages(faqPage.getTotalPages())
			.currentPage(faqPage.getNumber())
			.currentSize(faqPage.getNumberOfElements())
			.hasNextPage(faqPage.hasNext())
			.build();
	}

	// Response DTO : FaqResponseDTO.Faq
	public static FaqResponseDTO.Faq toFaq(Faq faq) {

		return FaqResponseDTO.Faq.builder()
			.faqId(faq.getId())
			.title(faq.getTitle())
			.content(faq.getContent())
			.build();
	}
}
