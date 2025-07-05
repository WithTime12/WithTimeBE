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
    @Column(name = "region_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "grid_x")
    private BigDecimal gridX;

    @Column(name = "grid_y")
    private BigDecimal gridY;

    @Column(name = "reg_code")
    private String regCode;
}
