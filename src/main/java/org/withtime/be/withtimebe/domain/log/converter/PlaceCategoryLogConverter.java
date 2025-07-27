package org.withtime.be.withtimebe.domain.log.converter;

import java.util.Comparator;
import java.util.List;

import org.withtime.be.withtimebe.domain.log.dto.PlaceCategoryLogResponseDTO;
import org.withtime.be.withtimebe.domain.log.model.PlaceCategoryLog;

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
}
