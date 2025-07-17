package org.withtime.be.withtimebe.domain.date.entity;

import jakarta.persistence.*;
import lombok.*;

import org.withtime.be.withtimebe.global.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "date_place_date_course")
public class DatePlaceDateCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_place_date_course_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_course_id", nullable = false)
    private DateCourse dateCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_place_id", nullable = false)
    private DatePlace datePlace;
}
