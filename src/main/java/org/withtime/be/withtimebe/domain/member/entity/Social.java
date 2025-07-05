package org.withtime.be.withtimebe.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.domain.member.entity.enums.SocialType;
import org.withtime.be.withtimebe.global.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "social")
public class Social extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_id")
    private Long id;

    @Column(name = "social_type", nullable = false)
    private SocialType socialType;

    @Column(name = "provider_id")
    private String providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
