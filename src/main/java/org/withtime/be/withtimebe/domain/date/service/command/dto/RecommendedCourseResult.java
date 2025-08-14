package org.withtime.be.withtimebe.domain.date.service.command.dto;

import org.withtime.be.withtimebe.domain.date.entity.DatePlace;

import java.util.List;

public record RecommendedCourseResult(
        List<DatePlace> places,   // 최종 코스 장소들(순서 유지)
        String signature          // "장소ID-장소ID-..." (중복 방지용)
) {}