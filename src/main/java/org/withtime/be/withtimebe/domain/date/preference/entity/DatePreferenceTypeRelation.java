package org.withtime.be.withtimebe.domain.date.preference.entity;

import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceRelationType;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceType;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "date_preference_type_relation")
public class DatePreferenceTypeRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_preference_type_relation_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PreferenceType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    private PreferenceType targetType;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "preference_relation_type")
    private PreferenceRelationType preferenceRelationType;

}
