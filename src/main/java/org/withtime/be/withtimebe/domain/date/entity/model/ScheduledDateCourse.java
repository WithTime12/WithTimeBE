package org.withtime.be.withtimebe.domain.date.entity.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ScheduledDateCourse implements Comparable<ScheduledDateCourse> {
    private List<ScheduledDatePlace> scheduledDatePlaces;
    private double weight;

    @Override
    public int compareTo(ScheduledDateCourse o) {
        return (int)(this.weight - o.weight);
    }
}
