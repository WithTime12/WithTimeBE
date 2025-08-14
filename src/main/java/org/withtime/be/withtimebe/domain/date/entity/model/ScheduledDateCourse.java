// ScheduledDateCourse
package org.withtime.be.withtimebe.domain.date.entity.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
public class ScheduledDateCourse implements Comparable<ScheduledDateCourse> {

    private final List<ScheduledDatePlace> scheduledDatePlaces;
    private final double weight; // 코스 총점

    @Override
    public int compareTo(ScheduledDateCourse o) {
        return Double.compare(this.weight, o.weight);
    }

    public static final Comparator<ScheduledDateCourse> BY_WEIGHT_ASC =
            Comparator.comparingDouble(ScheduledDateCourse::getWeight);
    public static final Comparator<ScheduledDateCourse> BY_WEIGHT_DESC =
            BY_WEIGHT_ASC.reversed();

    public ScheduledDateCourse(List<ScheduledDatePlace> scheduledDatePlaces, double weight) {
        this.scheduledDatePlaces = Objects.requireNonNull(scheduledDatePlaces, "scheduledDatePlaces must not be null");
        this.weight = weight;
    }
}
