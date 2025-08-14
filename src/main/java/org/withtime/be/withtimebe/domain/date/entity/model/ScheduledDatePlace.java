// ScheduledDatePlace
package org.withtime.be.withtimebe.domain.date.entity.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.withtime.be.withtimebe.domain.date.entity.DatePlace;
import org.withtime.be.withtimebe.domain.date.entity.enums.PlaceType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
public class ScheduledDatePlace {

    private final DatePlace datePlace;

    // 시간은 점수 산정 단계에선 없어도 됨(선택)
    private final LocalDateTime startTime; // nullable
    private final LocalDateTime endTime;   // nullable

    private final double score;

    @Builder(toBuilder = true)
    private ScheduledDatePlace(DatePlace datePlace,
                               LocalDateTime startTime,
                               LocalDateTime endTime,
                               double score) {
        this.datePlace = Objects.requireNonNull(datePlace, "datePlace must not be null");
        this.startTime = startTime;
        this.endTime = endTime;
        this.score = score;
    }

    // 시간 없이 점수만 설정
    public static ScheduledDatePlace ofScoreOnly(DatePlace place, double score) {
        return ScheduledDatePlace.builder()
                .datePlace(place)
                .score(score)
                .build();
    }

    // 시작시각 주면 placeType duration으로 종료시각 계산
    public ScheduledDatePlace withScheduleFrom(LocalDateTime start) {
        PlaceType type = datePlace.getPlaceType();
        Duration dur = (type != null) ? type.getDuration() : Duration.ZERO;
        LocalDateTime end = (start != null) ? start.plus(dur) : null;
        return this.toBuilder()
                .startTime(start)
                .endTime(end)
                .build();
    }

    public Duration getDuration() {
        if (startTime != null && endTime != null) {
            return Duration.between(startTime, endTime);
        }
        PlaceType type = datePlace.getPlaceType();
        return (type != null) ? type.getDuration() : Duration.ZERO;
    }
}
