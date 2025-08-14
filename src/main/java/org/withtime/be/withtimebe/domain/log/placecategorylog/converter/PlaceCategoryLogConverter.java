package org.withtime.be.withtimebe.domain.log.placecategorylog.converter;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.withtime.be.withtimebe.domain.date.entity.PlaceCategory;
import org.withtime.be.withtimebe.domain.log.placecategorylog.dto.PlaceCategoryLogResponseDTO;
import org.withtime.be.withtimebe.domain.log.placecategorylog.model.PlaceCategoryLog;

public class PlaceCategoryLogConverter {

	public static PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLogList toWeeklyPlaceCategoryLogList(List<PlaceCategoryLog> placeCategoryLogList) {

		// 1. 키워드 별 count 합산
		Map<String, Integer> countPerKeyword = placeCategoryLogList.stream()
			.collect(Collectors.groupingBy(
				PlaceCategoryLog::getPlaceCategoryLabel,
				Collectors.summingInt(PlaceCategoryLog::getCount)
			));

		// 2. DTO 변환
		List<PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLog> weeklyPlaceCategoryLogList = countPerKeyword.entrySet().stream()
			.map((entry) -> toWeeklyPlaceCategoryLog(entry.getKey(), entry.getValue()))
			.sorted(Comparator.comparing(PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLog::count).reversed()) // count 기준 내림차순
			.toList();

		return PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLogList.builder()
			.placeCategoryLogList(weeklyPlaceCategoryLogList)
			.build();
	}

	public static PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLog toWeeklyPlaceCategoryLog(String placeCategoryLabel, Integer count) {
		return PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLog.builder()
			.placeCategoryLabel(placeCategoryLabel)
			.count(count)
			.build();
	}

	public static PlaceCategoryLog toPlaceCategoryLog(String placeCategoryLabel, Integer count, LocalDate date) {
		return PlaceCategoryLog.builder()
			.placeCategoryLabel(placeCategoryLabel)
			.count(count)
			.date(date)
			.build();
	}
}
