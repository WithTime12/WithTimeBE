package org.withtime.be.withtimebe.domain.member.dto;

import lombok.Builder;

public record MemberResponseDTO() {
    @Builder
    public record ChangeInfo(
            String username
    ) {}

    @Builder
    public record FindMyGrade(
        String username,
        String level,
        String description,
        Integer nextRequiredPoint
    ) {}
}
