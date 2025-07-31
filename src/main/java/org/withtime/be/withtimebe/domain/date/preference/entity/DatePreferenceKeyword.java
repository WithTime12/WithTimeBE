package org.withtime.be.withtimebe.domain.date.preference.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "date_preference_keyword")
public class DatePreferenceKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_preference_keyword_id")
    private Long id;

    @Column(name = "keyword")
    private String keyword;

}
