package org.withtime.be.withtimebe.domain.date.preference.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "date_preference_description_keyword")
public class DatePreferenceDescriptionKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_preference_description")
    private DatePreferenceDescription datePreferenceDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_preference_keyword")
    private DatePreferenceKeyword datePreferenceKeyword;
}
