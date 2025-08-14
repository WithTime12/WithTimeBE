package org.withtime.be.withtimebe.domain.date.converter;

import org.springframework.data.domain.Page;
import org.withtime.be.withtimebe.domain.date.dto.request.DateRequestDTO;
import org.withtime.be.withtimebe.domain.date.dto.response.DateResponseDTO;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.date.entity.DateCourseBookmark;
import org.withtime.be.withtimebe.domain.date.entity.DatePlace;
import org.withtime.be.withtimebe.domain.date.entity.DatePlaceDateCourse;
import org.withtime.be.withtimebe.domain.member.entity.Member;

import java.time.LocalDateTime;
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
                .dateCourseId(dateCourseBookmark.getId())
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
    public static DateResponseDTO.DateCourse createDateCourseInfo(List<DatePlace> datePlaces, String signature){
        List<DateResponseDTO.DatePlace> datePlaceDtos = datePlaces.stream()
                .map(dp -> DateConverter.createDatePlace(dp, null, null))
                .toList();

        return DateResponseDTO.DateCourse.builder()
                .name(LocalDateTime.now().toLocalDate().toString())
                .datePlaces(datePlaceDtos)
                .signature(signature) // ← 추가
                .build();
    }

    // DatePlace -> DateResponseDTO.DatePlace
    public static DateResponseDTO.DatePlace createDatePlace(DatePlace datePlace,
                                                            LocalDateTime startTime,
                                                            LocalDateTime endTime) {
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
                .build();
    }

    // DateResponseDTO.DateCourse -> DateResponseDTO.DateCourse
    public static DateResponseDTO.DateCourse createDateCourse(DateCourse dateCourse,
                                                              Set<Long> bookmarkedIds,
                                                              DateRequestDTO.DateCourseSearchCond cond){
        Boolean bookmarked = null;
        if (!bookmarkedIds.isEmpty()) bookmarked = bookmarkedIds.contains(dateCourse.getId());
        List<DateResponseDTO.DatePlace> datePlaces = dateCourse.getDatePlaceDateCourses().stream()
                .map(dc -> DateConverter.createDatePlace(dc.getDatePlace(), dc.getStartTime(), dc.getEndTime()))
                .toList();

        return DateResponseDTO.DateCourse.builder()
                .name(dateCourse.getName())
                .datePlaces(datePlaces)
                .isBookmarked(bookmarked)
                .dateCourseSearchCondInfo(createSearchCond(cond))
                .build();
    }

    public static DateResponseDTO.DateCourseSearchCondInfo createSearchCond(DateRequestDTO.DateCourseSearchCond cond){
        return DateResponseDTO.DateCourseSearchCondInfo.builder()
                .budget(cond.budget())
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

}
