package org.withtime.be.withtimebe.domain.date.dto.response;

import lombok.Builder;
import org.withtime.be.withtimebe.domain.date.entity.enums.PlaceType;

import java.util.List;

public record DateResponseDTO() {

    @Builder
    public record DateCourseBookmark(
            Long dateCourseId
    ){}

    @Builder
    public record DatePlace(
        String name,
        String image,
        String tel,
        Integer averagePrice,
        String information,
        double latitude,
        double longitude,
        String roadNameAddress,
        String lotNumberAddress,
        PlaceType placeType
    ){}

    @Builder
    public record DateCourse(
        Long dateCourseId,
        String name,
        List<DateResponseDTO.DatePlace> datePlaces
    ){}

    @Builder
    public record DateCourseList(
            List<DateResponseDTO.DateCourse> dateCourseList,
            Integer totalPages,
            Integer currentPage,
            Integer currentSize,
            Boolean hasNextPage
    ){}
}
