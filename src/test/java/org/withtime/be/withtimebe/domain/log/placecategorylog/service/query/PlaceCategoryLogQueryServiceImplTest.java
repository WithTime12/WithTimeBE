package org.withtime.be.withtimebe.domain.log.placecategorylog.service.query;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.withtime.be.withtimebe.domain.log.placecategorylog.model.PlaceCategoryLog;
import org.withtime.be.withtimebe.domain.log.placecategorylog.repository.PlaceCategoryLogRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("[PlaceCategoryLogQueryService] 단위 테스트")
class PlaceCategoryLogQueryServiceImplTest {

	@InjectMocks
	private PlaceCategoryLogQueryServiceImpl placeCategoryLogQueryService;

	@Mock
	private PlaceCategoryLogRepository placeCategoryLogRepository;

	@Nested
	@DisplayName("findWeeklyPlaceCategoryLogList()")
	class FindWeeklyPlaceCategoryLogListTest {

		@Test
		@DisplayName("이번 주 월요일부터 일요일까지, 조회된 placeCategoryLog를 반환한다.")
		void find_weekly_placeCategoryLogList_test() {
			// given
			LocalDate now = LocalDate.now();
			LocalDate monday = now.with(DayOfWeek.MONDAY);
			LocalDate sunday = now.with(DayOfWeek.SUNDAY);

			PlaceCategoryLog expectedLog1 = PlaceCategoryLog.builder()
				.placeCategoryId(1L)
				.placeCategoryLabel("감성적인")
				.date(monday)
				.count(30)
				.build();
			PlaceCategoryLog expectedLog2 = PlaceCategoryLog.builder()
				.placeCategoryId(2L)
				.placeCategoryLabel("잔잔한")
				.date(monday.plusDays(1))
				.count(50)
				.build();

			given(placeCategoryLogRepository.findByDateBetween(monday, sunday))
				.willReturn(List.of(expectedLog1, expectedLog2));

			// when
			List<PlaceCategoryLog> result = placeCategoryLogQueryService.findWeeklyPlaceCategoryLogList();

			// then
			assertThat(result).hasSize(2);
			assertThat(result).containsExactlyInAnyOrder(expectedLog1, expectedLog2);
		}
	}
}