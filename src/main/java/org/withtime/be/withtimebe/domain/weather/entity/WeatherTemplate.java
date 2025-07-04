package org.withtime.be.withtimebe.domain.weather.entity;


import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.domain.weather.entity.enums.PrecipCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.TempCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.Weather;
import org.withtime.be.withtimebe.global.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeatherTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_template_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "weather", nullable = false)
    private Weather weather;

    @Enumerated(EnumType.STRING)
    @Column(name = "temp_category", nullable = false)
    private TempCategory tempCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "precip_category", nullable = false)
    private PrecipCategory precipCategory;

    @Column(name = "message")
    private String message;

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "emoji")
    private String emoji;
}
