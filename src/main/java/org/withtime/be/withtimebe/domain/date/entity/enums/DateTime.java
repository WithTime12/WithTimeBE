package org.withtime.be.withtimebe.domain.date.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.withtime.be.withtimebe.global.error.code.DateCourseErrorCode;
import org.withtime.be.withtimebe.global.error.exception.DateCourseException;

@Getter
@AllArgsConstructor
public enum DateTime {
    ONETOTWO(2),
    THREETOFROUR(3),
    HALFDAY(4),
    ALLDAY(5);

    private final int value;
}
