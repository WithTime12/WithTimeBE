package org.withtime.be.withtimebe.domain.faq.service.query;

import org.springframework.data.domain.Page;
import org.withtime.be.withtimebe.domain.faq.dto.request.FaqRequestDTO;
import org.withtime.be.withtimebe.domain.faq.entity.Faq;

public interface FaqQueryService {
	Page<Faq> findFaqList(FaqRequestDTO.FindFaqList request);
	Page<Faq> findFaqListByKeyword(FaqRequestDTO.FindFaqListByKeyword request);
}
