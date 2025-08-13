package org.withtime.be.withtimebe.domain.date.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public enum PlaceType {
	TIME_EAT("식사", "식사 위주 장소", Duration.ofHours(1)),
	TIME_SEE("구경", "구경 위주 장소", Duration.ofMinutes(30)),
	TIME_CAFE("카페", "카페 위주 장소", Duration.ofMinutes(90)),
	;

	private final String label;
	private final String description;
	private final Duration duration;

	public static LocalTime addTo(LocalTime time, PlaceType placeType) {
		return time.plus(placeType.getDuration());
	}
}
