package org.withtime.be.withtimebe.domain.member.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

public class MemberResponseDTO {

	@Builder
	public record MembershipList(
		List<MemberResponseDTO.Membership> membershipList,	// 멤버십 목록
		Integer totalPages,	// 전체 페이지 개수
		Integer currentPage,	// 현재 페이지 번호
		Integer currentSize,	// 현재 페이지의 크기
		Boolean hasNextPage	// 다음 페이지 존재 여부
	) {}

	@Builder
	public record Membership(
		Long memberId,    // 회원 식별자 값
		String name,	// 사용자 닉네임
		Boolean hasMembership,	// 멤버십 유무
		Long membershipDuration,	// 멤버십 유지기간
		LocalDateTime createdAt    // 회원가입 일자
	) {}
}
