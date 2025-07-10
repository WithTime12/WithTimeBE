package org.withtime.be.withtimebe.domain.faq.service.command;

import org.withtime.be.withtimebe.domain.faq.dto.request.FaqRequestDTO;
import org.withtime.be.withtimebe.domain.faq.entity.Faq;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface FaqCommandService {

	Faq createFaq(FaqRequestDTO.CreateFaq request, Member member);

	Faq updateFaq(FaqRequestDTO.UpdateFaq request, Long faqId);

	void deleteFaq(Long faqId);
}
