package org.withtime.be.withtimebe.domain.date.preference.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceType;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.global.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "date_preference_test_result")
public class DatePreferenceTestResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_preference_test_result_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferencePartType")
    private PreferenceType preferenceType;

    @Column(name = "a_percentage")
    private Double aPercentage;

    @Column(name = "b_percentage")
    private Double bPercentage;

    @Column(name = "c_percentage")
    private Double cPercentage;

    @Column(name = "d_percentage")
    private Double dPercentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void mappingMember(Member member) {
        this.member = member;
    }

}
