package org.withtime.be.withtimebe.domain.faq.service.query;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.faq.dto.request.FaqRequestDTO;
import org.withtime.be.withtimebe.domain.faq.entity.Faq;
import org.withtime.be.withtimebe.domain.faq.repository.FaqRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class FaqQueryServiceImpl implements FaqQueryService {

	private final FaqRepository faqRepository;

	@Override
	public Page<Faq> findFaqList(FaqRequestDTO.FindFaqList request) {
		return faqRepository.findFaqListByFaqCategory(
			request.faqCategory(), request.pageable());
	}

	@Override
	public Page<Faq> findFaqListByKeyword(FaqRequestDTO.FindFaqListByKeyword request) {
		return faqRepository.findFaqListByFaqCategoryAndKeyword(
			request.faqCategory(), request.keyword(), request.pageable());
	}
}
