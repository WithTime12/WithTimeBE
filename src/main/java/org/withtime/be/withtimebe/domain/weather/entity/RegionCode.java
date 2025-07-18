package org.withtime.be.withtimebe.domain.weather.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "region_code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class RegionCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // region_code_id

    @Column(name = "land_reg_code", nullable = false, unique = true)
    private String landRegCode;    // 중기 육상 예보용 지역 코드

    @Column(name = "temp_reg_code", nullable = false, unique = true)
    private String tempRegCode;    // 중기 기온 예보용 지역 코드

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "regionCode", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Region> regions = new ArrayList<>();
}
