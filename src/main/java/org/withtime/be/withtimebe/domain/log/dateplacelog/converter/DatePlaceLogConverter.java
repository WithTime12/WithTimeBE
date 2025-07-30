package org.withtime.be.withtimebe.domain.log.dateplacelog.converter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.withtime.be.withtimebe.domain.log.dateplacelog.dto.DatePlaceLogResponseDTO;
import org.withtime.be.withtimebe.domain.log.dateplacelog.entity.DatePlaceLog;

public class DatePlaceLogConverter {

	public static DatePlaceLogResponseDTO.MonthlyDatePlaceLogList toMonthlyDatePlaceLogList(List<DatePlaceLog> datePlaceLogs) {

		List<DatePlaceLogResponseDTO.MonthlyDatePlaceLog> datePlaceLogList = datePlaceLogs.stream()
			.map(DatePlaceLogConverter::toMonthlyDatePlaceLog)
			.toList();

		return DatePlaceLogResponseDTO.MonthlyDatePlaceLogList.builder()
			.datePlaceLogList(datePlaceLogList)
			.build();
	}

	public static DatePlaceLogResponseDTO.MonthlyDatePlaceLog toMonthlyDatePlaceLog(DatePlaceLog datePlaceLog) {

		Long year = (long)datePlaceLog.getDate().getYear();
		Long month = (long)datePlaceLog.getDate().getMonthValue();

		return DatePlaceLogResponseDTO.MonthlyDatePlaceLog.builder()
			.year(year)
			.month(month)
			.count(datePlaceLog.getCount())
			.build();
	}

	public static DatePlaceLog toDatePlaceLog(LocalDate date, Long count) {
		return DatePlaceLog.builder()
			.date(date)
			.count(count)
			.build();
	}
}
