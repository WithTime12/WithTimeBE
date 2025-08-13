package org.withtime.be.withtimebe.domain.log.placecategorylog.converter;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.withtime.be.withtimebe.domain.date.entity.PlaceCategory;
import org.withtime.be.withtimebe.domain.log.placecategorylog.dto.PlaceCategoryLogResponseDTO;
import org.withtime.be.withtimebe.domain.log.placecategorylog.model.PlaceCategoryLog;

public class PlaceCategoryLogConverter {

	public static PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLogList toWeeklyPlaceCategoryLogList(List<PlaceCategoryLog> placeCategoryLogList) {

		List<PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLog> weeklyPlaceCategoryLogList = placeCategoryLogList.stream()
			.sorted(Comparator.comparing(PlaceCategoryLog::getCount).reversed()) // count 기준 내림차순
			.map(PlaceCategoryLogConverter::toWeeklyPlaceCategoryLog)
			.toList();

		return PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLogList.builder()
			.placeCategoryLogList(weeklyPlaceCategoryLogList)
			.build();
	}

	public static PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLog toWeeklyPlaceCategoryLog(PlaceCategoryLog placeCategoryLog) {
		return PlaceCategoryLogResponseDTO.WeeklyPlaceCategoryLog.builder()
			.placeCategoryLabel(placeCategoryLog.getPlaceCategoryLabel())
			.count(placeCategoryLog.getCount())
			.build();
	}

	public static PlaceCategoryLog toPlaceCategoryLog(PlaceCategory placeCategory, Integer count, LocalDate date) {
		return PlaceCategoryLog.builder()
			.placeCategoryId(placeCategory.getId())
			.placeCategoryLabel(placeCategory.getLabel())
			.count(count)
			.date(date)
			.build();
	}
}
