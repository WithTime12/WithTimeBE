package org.withtime.be.withtimebe.domain.date.converter;

import lombok.Builder;
import org.springframework.data.domain.Page;
import org.withtime.be.withtimebe.domain.date.dto.request.DateRequestDTO;
import org.withtime.be.withtimebe.domain.date.dto.response.DateResponseDTO;
import org.withtime.be.withtimebe.domain.date.entity.*;
import org.withtime.be.withtimebe.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DateConverter {

    // DateCourse, Member -> DateCourseBookmark 엔티티 생성
    public static DateCourseBookmark createDateCourseBookmark(DateCourse dateCourse, Member member) {
        return DateCourseBookmark.builder()
                .member(member)
                .dateCourse(dateCourse)
                .build();
    }

    // DateCourseBookmark -> DateCourseBookmark ResponseDTO 생성
    public static DateResponseDTO.DateCourseBookmark createDateCourseBookmarkResponseDTO(DateCourseBookmark dateCourseBookmark) {
        return DateResponseDTO.DateCourseBookmark.builder()
                .dateCourseId(dateCourseBookmark.getDateCourse().getId())
                .build();
    }

    // DateRequestDTO.SaveDateCourse -> DateCourse
    public static DateCourse createDateCourse(DateRequestDTO.SaveDateCourse dateCourse){
        return DateCourse.builder()
                .name(dateCourse.name())
                .build();
    }

    // List<DatePlace> -> DateResponseDTO.DateCourseInfo
    // 단일 추천 코스를 응답으로 구성(시그니처 포함)
    public static DateResponseDTO.DateCourse createDateCourseInfo(List<DatePlace> datePlaces, String signature, DateRequestDTO.CreateDateCourse request){

        List<DateResponseDTO.DatePlace> datePlaceDtos = datePlaces.stream()
                .map(dp -> {
                    LocalDateTime startTime = request.startTime();

                    int index = datePlaces.indexOf(dp);
                    LocalDateTime thisStartTime = startTime.plusMinutes(90L * index);
                    LocalDateTime endTime = thisStartTime.plusMinutes(90);

                    return DateConverter.createDatePlace(dp, thisStartTime, endTime);
                })
                .toList();

        return DateResponseDTO.DateCourse.builder()
                .name(LocalDateTime.now().toLocalDate().toString())
                .datePlaces(datePlaceDtos)
                .dateCourseSearchCondInfo(toDateCourseSearchCondInfo(request))
                .signature(signature) // ← 추가
                .build();
    }

    public static DateResponseDTO.DateCourseSearchCondInfo toDateCourseSearchCondInfo(DateRequestDTO.CreateDateCourse request) {
        return DateResponseDTO.DateCourseSearchCondInfo.builder()
                .budget(request.budget())                       // 예산 구간
                .datePlaces(request.datePlaces())               // 선택한 장소 리스트
                .dateDurationTime(request.dateDurationTime())   // 데이트 소요 시간
                .mealTypes(request.mealPlan())                  // 식사 계획
                .transportation(request.transportation())       // 교통 수단
                .userPreferredKeywords(request.userPreferredKeywords()) // 키워드
                .build();
    }

    // DatePlace -> DateResponseDTO.DatePlace
    public static DateResponseDTO.DatePlace createDatePlace(DatePlace datePlace,
                                                            LocalTime startTime,
                                                            LocalTime endTime) {
        Item item = null;
        if (datePlace.getItems() != null && !datePlace.getItems().isEmpty()) {
            item = datePlace.getItems().get(0);
        }

        return DateResponseDTO.DatePlace.builder()
                .datePlaceId(datePlace.getId())
                .name(datePlace.getName())
                .image(datePlace.getImage())
                .tel(datePlace.getTel())
                .averagePrice(datePlace.getAveragePrice())
                .information(datePlace.getInformation())
                .latitude(datePlace.getLatitude())
                .longitude(datePlace.getLongitude())
                .roadNameAddress(datePlace.getRoadNameAddress())
                .lotNumberAddress(datePlace.getLotNumberAddress())
                .placeType(datePlace.getPlaceType())
                .startTime(startTime)
                .endTime(endTime)
                .signatureDish(toSignatureDish(item))
                .build();
    }

    public static DateResponseDTO.DatePlace createDatePlace(DatePlace datePlace,
                                                            LocalDateTime startTime,
                                                            LocalDateTime endTime) {
        List<DateResponseDTO.PlaceCategoryResponse> placeCategoryResponseList = datePlace.getPlaceCategories().stream().map((d) ->
            DateResponseDTO.PlaceCategoryResponse.builder()
                    .placeCategoryType(d.getPlaceCategory().getCategoryType())
                    .label(d.getPlaceCategory().getLabel())
                    .code(d.getPlaceCategory().getCode())
                    .description(d.getPlaceCategory().getDescription())
                    .build()
        ).toList();

        return DateResponseDTO.DatePlace.builder()
                .datePlaceId(datePlace.getId())
                .name(datePlace.getName())
                .image(datePlace.getImage())
                .tel(datePlace.getTel())
                .averagePrice(datePlace.getAveragePrice())
                .information(datePlace.getInformation())
                .latitude(datePlace.getLatitude())
                .longitude(datePlace.getLongitude())
                .roadNameAddress(datePlace.getRoadNameAddress())
                .lotNumberAddress(datePlace.getLotNumberAddress())
                .placeType(datePlace.getPlaceType())
                .startTime(LocalTime.from(startTime))
                .endTime(LocalTime.from(endTime))
                .signatureDish(toSignatureDish(datePlace.getItems() == null || datePlace.getItems().isEmpty() ? null : datePlace.getItems().get(0)))
                .placeCategoryResponseList(placeCategoryResponseList)
                .build();
    }

    // DateResponseDTO.DateCourse -> DateResponseDTO.DateCourse
    public static DateResponseDTO.DateCourse createDateCourse(DateCourse dateCourse,
                                                              Set<Long> bookmarkedIds,
                                                              DateRequestDTO.DateCourseSearchCond cond){
        Boolean bookmarked = null;
        if (bookmarkedIds != null && !bookmarkedIds.isEmpty()) bookmarked = bookmarkedIds.contains(dateCourse.getId());
        List<DateResponseDTO.DatePlace> datePlaces = dateCourse.getDatePlaceDateCourses().stream()
                .map(dc -> DateConverter.createDatePlace(dc.getDatePlace(), dc.getStartTime(), dc.getEndTime()))
                .toList();

        return DateResponseDTO.DateCourse.builder()
                .dateCourseId(dateCourse.getId())
                .name(dateCourse.getName())
                .datePlaces(datePlaces)
                .isBookmarked(bookmarked)
                .dateCourseSearchCondInfo(createSearchCond(cond))
                .build();
    }

    public static DateResponseDTO.DateCourseSearchCondInfo createSearchCond(DateRequestDTO.DateCourseSearchCond cond){
        return DateResponseDTO.DateCourseSearchCondInfo.builder()
                .budget(cond.datePriceRange())
                .datePlaces(cond.datePlaces())
                .mealTypes(cond.mealTypes())
                .transportation(cond.transportation())
                .dateDurationTime(cond.dateDurationTime())
                .userPreferredKeywords(cond.userPreferredKeywords())
                .build();
    }

    // Page<DateCourse> -> DateRequestDTO.DateCourseList
    public static DateResponseDTO.DateCourseList createDateCourseList(Page<DateCourse> dateCourses, Set<Long> bookmarkedIds,
                                                                      DateRequestDTO.DateCourseSearchCond cond){
        List<DateResponseDTO.DateCourse> dateCourseList = dateCourses.stream()
                .map(dc ->{
                        if (bookmarkedIds != null) return createDateCourse(dc, bookmarkedIds, cond);
                        return createDateCourse(dc, Collections.emptySet(), cond);
                })
                .toList();

        return DateResponseDTO.DateCourseList.builder()
                .dateCourseList(dateCourseList)
                .totalPages(dateCourses.getTotalPages())
                .currentPage(dateCourses.getNumber())
                .currentSize(dateCourses.getSize())
                .hasNextPage(dateCourses.hasNext())
                .totalCount(dateCourses.getTotalElements())
                .build();
    }

    @Builder
    public static DateResponseDTO.SignatureDish toSignatureDish(Item item) {
        if (item == null) return null;

        return DateResponseDTO.SignatureDish.builder()
                .ItemId(item.getId())
                .imageUrl(item.getImage())
                .price(item.getPrice())
                .name(item.getName())
                .build();
    }

}
