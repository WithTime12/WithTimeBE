package org.withtime.be.withtimebe.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.domain.member.entity.enums.AlarmType;
import org.withtime.be.withtimebe.global.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "alarm")
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_type")
    private AlarmType alarmType;

    @Column(name = "target_key")
    private Long targetKey;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
