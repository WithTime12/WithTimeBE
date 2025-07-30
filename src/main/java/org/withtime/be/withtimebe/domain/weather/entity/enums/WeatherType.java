package org.withtime.be.withtimebe.domain.weather.entity.enums;

public enum WeatherType {
    CLEAR,              // 맑음
    CLOUDY,             // 구름 많음, 흐림
    RAINY,              // 비 (구름 많고 비, 흐리고 비)
    SNOWY,              // 눈 (구름 많고 눈, 흐리고 눈)
    RAIN_SNOW,          // 비/눈 (흐리고 비/눈, 구름 많고 비/눈)
    SHOWER              // 소나기 (흐리고 소나기, 구름 많고 소나기)
}
