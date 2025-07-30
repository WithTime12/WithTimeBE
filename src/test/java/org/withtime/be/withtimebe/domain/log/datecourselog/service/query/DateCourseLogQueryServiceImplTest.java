package org.withtime.be.withtimebe.domain.log.datecourselog.service.query;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.withtime.be.withtimebe.domain.date.repository.DateCourseRepository;
import org.withtime.be.withtimebe.domain.log.datecourselog.dto.DateCourseLogResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("[DateCourseLogQueryService] 단위 테스트")
class DateCourseLogQueryServiceImplTest {

	@InjectMocks
	private DateCourseLogQueryServiceImpl dateCourseLogQueryService;

	@Mock
	private DateCourseRepository dateCourseRepository;

	@Mock
	private MemberRepository memberRepository;

	@Nested
	@DisplayName("findAverageDateCourseCount()")
	class FindAverageDateCourseCount {

		@Test
		@DisplayName("member가 성공적으로 주어지면 평균 데이트 횟수와 나의 데이트 횟수가 계산된다.")
		void returnsAverageWithMember() {
			// given
			Long memberId = 1L;

			Member member = Member.builder()
				.id(memberId)
				.build();

			LocalDate now = LocalDate.now();
			LocalDate oneMonthAgo = now.minusDays(30);

			given(dateCourseRepository.countByCreatedAtBetween(oneMonthAgo, now)).willReturn(300L);
			given(memberRepository.count()).willReturn(15L);
			given(dateCourseRepository.countByMemberId(memberId)).willReturn(10L);	// 횟수 : 10번

			// when
			DateCourseLogResponseDTO.FindAverageDateCourseCount result =
				dateCourseLogQueryService.findAverageDateCourseCount(member);

			// then
			assertThat(result.averageDateCount()).isEqualTo(20.0);
			assertThat(result.myDateCount()).isEqualTo(10L);
		}

		@Test
		@DisplayName("비회원인 경우 (member == null) 나의 데이트 횟수는 0으로 계산된다.")
		void returnsAverageWithNullMember() {
			// given
			LocalDate now = LocalDate.now();
			LocalDate oneMonthAgo = now.minusDays(30);

			given(dateCourseRepository.countByCreatedAtBetween(oneMonthAgo, now)).willReturn(300L);
			given(memberRepository.count()).willReturn(15L);

			// when
			DateCourseLogResponseDTO.FindAverageDateCourseCount result =
				dateCourseLogQueryService.findAverageDateCourseCount(null);

			// then
			assertThat(result.averageDateCount()).isEqualTo(20.0);
			assertThat(result.myDateCount()).isEqualTo(0L);
		}
	}
}