package org.withtime.be.withtimebe.domain.date.preference.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferencePartType;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "date_preference_part_description")
public class DatePreferencePartDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_preference_description_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "preference_part_type", unique = true)
    private PreferencePartType preferencePartType;

    @Column(name = "type_eng")
    private String typeEng;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;
}
