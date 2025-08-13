package org.withtime.be.withtimebe.domain.weather.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "raw_short_term_weather")
public class RawShortTermWeather extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "raw_short_term_weather_id")
    private Long id;

    @Column(name = "base_date", nullable = false)
    private LocalDate baseDate;

    @Column(name = "base_time", nullable = false)
    private String baseTime;

    @Column(name = "fcst_date", nullable = false)
    private LocalDate forecastDate;

    @Column(name = "fcst_time", nullable = false)
    private String forecastTime;

    @Column(name = "tmp")
    private double temperature;

    @Column(name = "sky")
    private String sky;

    @Column(name = "pop")
    private double precipitationProbability;

    @Column(name = "pty")
    private String precipitationType;

    @Column(name = "pcp")
    private double precipitationAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    public void updateWeatherData(Double tmp, String sky, Double pop, String pty, Double pcp) {
        this.temperature = tmp;
        this.sky = sky;
        this.precipitationProbability = pop;
        this.precipitationType = pty;
        this.precipitationAmount = pcp;
    }
}
