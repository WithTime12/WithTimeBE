package org.withtime.be.withtimebe.domain.weather.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "region")
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;           // region_id

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "grid_x", nullable = false, precision = 5, scale = 2)
    private BigDecimal gridX;

    @Column(name = "grid_y", nullable = false, precision = 5, scale = 2)
    private BigDecimal gridY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_code_id", nullable = false)
    private RegionCode regionCode;
}
