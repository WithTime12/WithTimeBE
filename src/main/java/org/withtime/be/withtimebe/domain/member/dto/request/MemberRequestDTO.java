package org.withtime.be.withtimebe.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

public class MemberRequestDTO {

	@Builder
	public record UpdateMembership(
		@NotNull(message = "멤버십 연장 일수를 입력해주세요")
		@PositiveOrZero(message = "0 이상의 수를 입력해주세요")
		Long extendDays,
		@NotNull(message = "멤비십 취소 여부를 입력해주세요")
		Boolean cancelMembership
	) {}
}
