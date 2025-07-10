package org.withtime.be.withtimebe.domain.faq.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.faq.converter.FaqConverter;
import org.withtime.be.withtimebe.domain.faq.dto.request.FaqRequestDTO;
import org.withtime.be.withtimebe.domain.faq.entity.Faq;
import org.withtime.be.withtimebe.domain.faq.repository.FaqRepository;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.global.error.code.FaqErrorCode;
import org.withtime.be.withtimebe.global.error.exception.FaqException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(readOnly = false)
public class FaqCommandServiceImpl implements FaqCommandService {

	private FaqRepository faqRepository;

	@Override
	public Faq createFaq(FaqRequestDTO.CreateFaq request, Member member) {
		Faq faq = FaqConverter.toFaqEntity(request, member);
		return faqRepository.save(faq);
	}

	@Override
	public Faq updateFaq(FaqRequestDTO.UpdateFaq request) {
		Faq faq = faqRepository.findById(request.faqId())
			.orElseThrow(() -> new FaqException(FaqErrorCode.FAQ_NOT_FOUND));

		faq.updateFields(request);

		return faq;
	}
}
