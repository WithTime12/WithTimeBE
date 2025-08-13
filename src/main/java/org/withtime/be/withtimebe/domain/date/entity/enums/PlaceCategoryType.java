package org.withtime.be.withtimebe.domain.date.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum     PlaceCategoryType {
    MOOD("분위기"),
    ACTIVITY_TYPE("활동량"),
    PLACE_STYLE("장소 스타일"),
    CONTEXTUAL_CONDITION("상황/시간대/날씨"),
    ;

    private final String label;
}
