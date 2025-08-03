package org.withtime.be.withtimebe.domain.date.preference.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceType;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "date_preference_description")
public class DatePreferenceDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_preference_description_id")
    private Long id;

    @Column(name = "symbolic_animal")
    private String symbolicAnimal;

    @Enumerated(EnumType.STRING)
    @Column(name = "preference_type", unique = true)
    private PreferenceType preferenceType;

    @Column(name = "simple_description")
    private String simpleDescription;

    @Column(name = "analysis", columnDefinition = "TEXT")
    private String analysis;
}
