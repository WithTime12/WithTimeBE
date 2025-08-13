package org.withtime.be.withtimebe.domain.log.dateplacelog.service.query;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.withtime.be.withtimebe.domain.log.dateplacelog.model.DatePlaceLog;

@ExtendWith(MockitoExtension.class)
@DisplayName("[DatePlaceLogQueryService] 단위 테스트")
class DatePlaceLogQueryServiceImplTest {

	@InjectMocks
	private DatePlaceLogQueryServiceImpl datePlaceLogQueryService;

	@Mock
	private MongoTemplate mongoTemplate;

	@Nested
	@DisplayName("findMonthlyDatePlaceLogList()")
	class FindMonthlyDatePlaceLogList {

		@Test
		@DisplayName("Aggregation을 사용하여 월별 DatePlace 개수를 반환한다.")
		void returnsMonthlyDatePlaceLogList() {
			// given
			DatePlaceLog log1 = DatePlaceLog.builder()
				.date(LocalDate.of(2025, 6, 1))
				.count(300L)
				.build();
			DatePlaceLog log2 = DatePlaceLog.builder()
				.date(LocalDate.of(2025, 7, 1))
				.count(350L)
				.build();
			List<DatePlaceLog> expectedLogs = List.of(log1, log2);

			AggregationResults<DatePlaceLog> aggregationResult = new AggregationResults<>(expectedLogs, new Document());

			given(mongoTemplate.aggregate(any(Aggregation.class), eq("date_place_logs"), eq(DatePlaceLog.class))).willReturn(aggregationResult);

			// when
			List<DatePlaceLog> result = datePlaceLogQueryService.findMonthlyDatePlaceLogList();

			// then
			assertThat(result).hasSize(2);
			assertThat(result).containsExactly(log1, log2);
			verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), eq("date_place_logs"), eq(DatePlaceLog.class));
		}
	}

}