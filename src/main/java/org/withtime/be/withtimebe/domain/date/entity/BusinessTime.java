package org.withtime.be.withtimebe.domain.date.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.domain.date.entity.enums.Day;
import org.withtime.be.withtimebe.domain.dateplace.entity.DatePlace;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "business_time")
public class BusinessTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_time_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_place_id", nullable = false)
    private DatePlace datePlace;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "break_start_time")
    private LocalTime breakStartTime;

    @Column(name = "break_end_time")
    private LocalTime breakEndTime;

    @Column(name = "last_order_time")
    private LocalTime lastOrderTime;

    @Enumerated(EnumType.STRING)
    private Day day;
}
