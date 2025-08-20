package org.withtime.be.withtimebe.domain.log.dateplacelog.service.query;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.log.dateplacelog.model.DatePlaceLog;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DatePlaceLogQueryServiceImpl implements DatePlaceLogQueryService {
	
	private final MongoTemplate mongoTemplate;

	@Override
	@Cacheable(
		value = "date-place-log",
		key = "T(java.time.LocalDate).now().getYear() + '-' + " +
			"T(java.time.LocalDate).now().get(" + "T(java.time.temporal.WeekFields).ISO.weekOfYear()" + ") + '-' + " +
			"T(java.time.LocalDate).now().getDayOfWeek().getValue()",
		cacheManager = "redisCacheManager"
	)
	public List<DatePlaceLog> findMonthlyDatePlaceLogList() {

		// 1. 추출할 필드 정의
		ProjectionOperation projectToMonth = Aggregation.project()
			.andExpression("date").as("date")
			.andExpression("count").as("count")
			.andExpression("year(date)").as("year")
			.andExpression("month(date)").as("month");

		// 2. date 기준 내림차순 정렬
		SortOperation sortByDateDesc = Aggregation.sort(Sort.Direction.DESC, "date");

		// 3. 월별 groupBy, 이후 최신 다큐먼트 하나 선택
		GroupOperation groupByYearMonth = Aggregation.group("year", "month")
			.first("date").as("date")
			.first("count").as("count");

		// 4. 결과 월별 오름차순 정렬
		SortOperation sortByYearMonthAsc = Aggregation.sort(Sort.by(Sort.Direction.ASC, "_id.year", "_id.month"));

		// 5. 파이프라인 생성
		Aggregation aggregation = Aggregation.newAggregation(
			projectToMonth,
			sortByDateDesc,
			groupByYearMonth,
			sortByYearMonthAsc
		);

		AggregationResults<DatePlaceLog> results =
			mongoTemplate.aggregate(
				aggregation,
				"date_place_logs",
				DatePlaceLog.class
			);

		return results.getMappedResults();
	}
}
