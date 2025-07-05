package org.withtime.be.withtimebe.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.domain.member.entity.enums.BillingStatus;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "payments")
public class Payments extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payments_id")
    private Long id;

    @Column(name = "billing_date", nullable = false)
    private LocalDateTime billingDate;

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @Column(name = "price", nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_status", nullable = false)
    private BillingStatus billingStatus;

    @Column(name = "approval_number", nullable = false)
    private String approvalNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
