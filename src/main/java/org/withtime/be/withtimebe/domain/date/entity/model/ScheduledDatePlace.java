package org.withtime.be.withtimebe.domain.date.entity.model;

import lombok.Builder;
import lombok.Getter;
import org.withtime.be.withtimebe.domain.date.entity.DatePlace;

import java.time.Duration;
import java.time.LocalTime;

@Getter
public class ScheduledDatePlace {
    private DatePlace datePlace;
    private LocalTime startTime;
    private LocalTime endTime;
    private double score;

    // 그리고 여기서 그냥 comparator 쓰면 되지 않나?

    @Builder
    public ScheduledDatePlace(DatePlace datePlace, LocalTime startTime, Duration duration, double score) {
        this.datePlace = datePlace;
        this.startTime = startTime;
        this.endTime = startTime.plus(duration);
        this.score = score;
    }

    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }
}
