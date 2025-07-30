package org.withtime.be.withtimebe.domain.weather.entity.enums;

public enum PrecipCategory {

    /**
     * 비 없음 (0%)
     * 맑거나 흐림
     */
    NONE,

    /**
     * 비 거의 없음 (1~30%)
     * 가볍게 산책 가능, 실외 일정 유지 가능
     */
    VERY_LOW,

    /**
     * 비 약간 가능성 (31~60%)
     * 우산 필요 가능성 있음, 유연한 동선 필요
     */
    LOW,

    /**
     * 비 올 가능성 높음 (61~90%)
     * 실외 지양, 실내 위주 일정 추천
     */
    HIGH,

    /**
     * 비 확실 (91~100%)
     * 실외 활동 지양, 완전 실내형 코스 구성
     */
    VERY_HIGH
}
