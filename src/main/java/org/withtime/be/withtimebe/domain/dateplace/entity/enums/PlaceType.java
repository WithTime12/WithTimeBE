package org.withtime.be.withtimebe.domain.dateplace.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlaceType {
	TIME_EAT("식사", "식사 위주 장소"),
	TIME_SEE("구경", "구경 위주 장소"),
	TIME_CAFE("카페", "카페 위주 장소"),
	;

	private final String label;
	private final String description;
}
