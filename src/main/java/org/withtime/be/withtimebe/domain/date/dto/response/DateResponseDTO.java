package org.withtime.be.withtimebe.domain.date.dto.response;

import lombok.Builder;

public record DateResponseDTO() {

    @Builder
    public record DateCourseBookmark(
            Long dateCourseId
    ){}
}
