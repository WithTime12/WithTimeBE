package org.withtime.be.withtimebe.domain.faq.service.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.faq.dto.request.FaqRequestDTO;
import org.withtime.be.withtimebe.domain.faq.entity.Faq;
import org.withtime.be.withtimebe.domain.faq.entity.enums.FaqCategory;

public interface FaqQueryService {
	Page<Faq> findFaqList(Pageable pageable, FaqCategory faqCategory);
	Page<Faq> findFaqListByKeyword(Pageable pageable, String keyword, FaqCategory faqCategory);
}
