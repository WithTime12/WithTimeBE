package org.withtime.be.withtimebe.domain.weather.entity;


import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.domain.weather.entity.enums.PrecipCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.TempCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.WeatherType;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "weather_template")
public class WeatherTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_template_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "weather", nullable = false)
    private WeatherType weatherType;

    @Enumerated(EnumType.STRING)
    @Column(name = "temp_category", nullable = false)
    private TempCategory tempCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "precip_category", nullable = false)
    private PrecipCategory precipCategory;

    @Column(name = "message")
    private String message;

    @Column(name = "emoji")
    private String emoji;

    @OneToMany(mappedBy = "weatherTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TemplateKeyword> templateKeywords = new ArrayList<>();
}
