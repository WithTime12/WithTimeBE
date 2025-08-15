package org.withtime.be.withtimebe.domain.date.dto.response;

import lombok.Builder;
import org.withtime.be.withtimebe.domain.date.entity.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record DateResponseDTO() {

    @Builder
    public record DateCourseBookmark(
            Long dateCourseId
    ){}

    @Builder
    public record DatePlace(
        Long datePlaceId,
        String name,
        String image,
        String tel,
        Integer averagePrice,
        String information,
        double latitude,
        double longitude,
        String roadNameAddress,
        String lotNumberAddress,
        PlaceType placeType,
        LocalTime startTime,
        LocalTime endTime,
        SignatureDish signatureDish,
        List<PlaceCategoryResponse> placeCategoryResponseList
    ){}

    @Builder
    public record DateCourse(
        Long dateCourseId,
        String name,
        List<DateResponseDTO.DatePlace> datePlaces,
        DateCourseSearchCondInfo dateCourseSearchCondInfo,
        Boolean isBookmarked,
        String signature
    ){}

    @Builder
    public record PlaceCategoryResponse (
            PlaceCategoryType placeCategoryType,
            String description,
            String code,
            String label
    ){}

    @Builder
    public record DateCourseList(
            List<DateResponseDTO.DateCourse> dateCourseList,
            Integer totalPages,
            Integer currentPage,
            Integer currentSize,
            Boolean hasNextPage,
            Long totalCount
    ){}

    @Builder
    public record DateCourseSearchCondInfo(
            DatePriceRange budget,
            List<String> datePlaces,
            DateTime dateDurationTime,
            List<MealType> mealTypes,
            Transportation transportation,
            List<String> userPreferredKeywords
    ){}

    @Builder
    public record SignatureDish(
            Long ItemId,
            String imageUrl,
            String name,
            String price
    ){}
}
