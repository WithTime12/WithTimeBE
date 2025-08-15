package org.withtime.be.withtimebe.domain.date.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.domain.date.entity.enums.DatePriceRange;
import org.withtime.be.withtimebe.domain.date.entity.enums.DateTime;
import org.withtime.be.withtimebe.domain.date.entity.enums.MealType;
import org.withtime.be.withtimebe.domain.date.entity.enums.Transportation;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "date_course")
public class DateCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_course_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "date_price_range")
    @Enumerated(EnumType.STRING)
    private DatePriceRange datePriceRange;

    @ElementCollection
    @CollectionTable(name ="date_places", joinColumns =
    @JoinColumn(name= "date_course_id"))
    @Builder.Default
    List<String> datePlaces= new ArrayList<>();

    @Column(name = "dateTime")
    @Enumerated(EnumType.STRING)
    private DateTime dateTime;

    @ElementCollection
    @CollectionTable(name = "course_meal_types",
    joinColumns = @JoinColumn(name = "date_course_id"))
    @Column(name = "meal_type")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<MealType> mealTypes= new ArrayList<>();

    @Column(name = "transportation")
    private Transportation transportation;

    @Builder.Default
    @OneToMany(mappedBy = "dateCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DateCoursePlaceCategory> DateCoursePlaceCategory = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "dateCourse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DatePlaceDateCourse> datePlaceDateCourses = new ArrayList<>();

    // 연관 관계 맵핑 메소드
    public void addDatePlaceDateCourses(List<DatePlaceDateCourse> datePlaceDateCourseList) {
        for (DatePlaceDateCourse datePlaceDateCourse : datePlaceDateCourseList) {
            datePlaceDateCourse.setDateCourse(this);
        }
        datePlaceDateCourses.addAll(datePlaceDateCourseList);
    }
}
