package org.withtime.be.withtimebe.domain.date.entity.enums;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum MealType {
    BREAKFAST(LocalTime.of(7, 0) , LocalTime.of(10, 0)),
    LUNCH(LocalTime.of(11, 0) , LocalTime.of(14, 0)),
    DINNER(LocalTime.of(17, 0) , LocalTime.of(20, 0))
    ;

    private final LocalTime startTime;
    private final LocalTime endTime;

    // 식사 시간 유효성 확인
    public boolean isInMealTime(LocalDateTime time){
        LocalTime t = time.toLocalTime();
        return !t.isBefore(this.startTime) && !t.isAfter(this.endTime);
    }

    // 시간으로 MealType 반환
    public static Optional<MealType> getMealTypeByTime(LocalDateTime time){
        return Arrays.stream(MealType.values())
                .filter(m -> m.isInMealTime(time))
                .findFirst();
    }

}
