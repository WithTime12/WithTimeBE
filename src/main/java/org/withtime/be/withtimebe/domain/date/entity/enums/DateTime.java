package org.withtime.be.withtimebe.domain.date.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateTime {
    ONETOTWO(2),
    THREETOFOUR(3),
    HALFDAY(4),
    ALLDAY(5);

    private final int value;
}
