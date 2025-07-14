package org.withtime.be.withtimebe.domain.member.converter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Page;
import org.withtime.be.withtimebe.domain.member.dto.response.MemberResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.Payments;
import org.withtime.be.withtimebe.domain.member.entity.enums.BillingStatus;
import org.withtime.be.withtimebe.domain.member.entity.enums.UserRank;

public class MemberConverter {

	// Response DTO : MemberResponseDTO.MembershipList
	public static MemberResponseDTO.MembershipList toMembershipList(Page<Member> memberPage) {

		List<MemberResponseDTO.Membership> memberList = memberPage.stream()
			.map(MemberConverter::toMembership)
			.toList();

		return MemberResponseDTO.MembershipList.builder()
			.membershipList(memberList)
			.totalPages(memberPage.getTotalPages())
			.currentPage(memberPage.getNumber())
			.currentSize(memberPage.getNumberOfElements())
			.hasNextPage(memberPage.hasNext())
			.build();
	}

	// Response DTO : MemberResponseDTO.Membership
	public static MemberResponseDTO.Membership toMembership(Member member) {

		List<Payments> paymentsList = member.getPaymentList();

		// 멤버십 보유 여부
		boolean hasMembership = member.getUserRank().equals(UserRank.PREMIUM);

		// 멤버십 총 가입 기간
		LocalDate today = LocalDate.now();

		Long totalDays = paymentsList.stream()
			.filter(payment -> payment.getBillingStatus().equals(BillingStatus.COMPLETED))
			.mapToLong(payment -> {
				LocalDate start = payment.getBillingDate().toLocalDate();
				LocalDate end = payment.getMembershipExpireDate().toLocalDate();
				if (end.isBefore(today)) return ChronoUnit.DAYS.between(start, end);	// 만료 멤버십
				else return ChronoUnit.DAYS.between(start, today);	// 아직 유효한 멤버십
			})
			.sum();

		return MemberResponseDTO.Membership.builder()
			.memberId(member.getId())
			.name(member.getNickname())
			.hasMembership(hasMembership)
			.membershipDuration(totalDays)
			.createdAt(member.getCreatedAt())
			.build();
	}
}
