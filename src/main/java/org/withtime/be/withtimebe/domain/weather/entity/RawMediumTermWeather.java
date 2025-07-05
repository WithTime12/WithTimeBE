package org.withtime.be.withtimebe.domain.weather.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "raw_medium_term_weather")
public class RawMediumTermWeather extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "raw_medium_term_weather_id")
    private Long id;

    @Column(name = "tmfc", nullable = false)
    private LocalDate baseDate;

    @Column(name = "tmef", nullable = false)
    private LocalDate forecastDate;

    @Column(name = "sky")
    private String sky;

    @Column(name = "pop")
    private double precipitationProbability;

    @Column(name = "min_tmp")
    private double minTemperature;

    @Column(name = "max_tmp")
    private double maxTemperature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;
}
