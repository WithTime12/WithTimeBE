package org.withtime.be.withtimebe.domain.faq.service.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.faq.dto.request.FaqRequestDTO;
import org.withtime.be.withtimebe.domain.faq.entity.Faq;
import org.withtime.be.withtimebe.domain.faq.entity.enums.FaqCategory;
import org.withtime.be.withtimebe.domain.faq.repository.FaqRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class FaqQueryServiceImpl implements FaqQueryService {

	private final FaqRepository faqRepository;

	@Override
	public Page<Faq> findFaqList(Pageable pageable, FaqCategory faqCategory) {
		return faqRepository.findFaqListByFaqCategory(
			faqCategory, pageable);
	}

	@Override
	public Page<Faq> findFaqListByKeyword(Pageable pageable, String keyword, FaqCategory faqCategory) {
		return faqRepository.findFaqListByFaqCategoryAndKeyword(
			faqCategory, keyword, pageable
		);
	}
}
