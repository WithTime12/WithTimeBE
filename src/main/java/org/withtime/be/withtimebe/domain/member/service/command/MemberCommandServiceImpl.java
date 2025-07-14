package org.withtime.be.withtimebe.domain.member.service.command;

import java.time.LocalDateTime;
import java.util.Comparator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.member.dto.request.MemberRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.Payments;
import org.withtime.be.withtimebe.domain.member.entity.enums.BillingStatus;
import org.withtime.be.withtimebe.domain.member.entity.enums.UserRank;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;
import org.withtime.be.withtimebe.global.error.code.MemberErrorCode;
import org.withtime.be.withtimebe.global.error.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class MemberCommandServiceImpl implements MemberCommandService {

	private final MemberRepository memberRepository;

	@Override
	public Member updateMembership(MemberRequestDTO.UpdateMembership request) {

		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

		// 가장 최근 멤버십 결제 정보 탐색
		Payments latestPayment = member.getPaymentList().stream()
			.filter(payment -> payment.getBillingStatus().equals(BillingStatus.COMPLETED))
			.max(Comparator.comparing(Payments::getMembershipExpireDate))
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBERSHIP_NOT_FOUND));

		if (request.extendDays() > 0) {
			latestPayment.updateExpireDate(latestPayment.getMembershipExpireDate().plusDays(request.extendDays()));
		}
		if (request.cancelMembership() == true) {
			latestPayment.updateExpireDate(LocalDateTime.now());
			member.updateUserRank(UserRank.COMMON);	// 일반 등급으로 변경
		}

		return member;
	}
}
