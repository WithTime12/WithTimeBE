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
import java.util.ArrayList;
import java.util.List;

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

    // 나중에 생성한 정보를 리턴하는 데 사용,,? 근데 애초에 그 뭐야
    // builder()로 만들 때 잘 만들어주면 안되냐
    // List<DatePlace> -> DateResponseDTO.DateCourseInfo
    // 단일 추천 코스를 응답으로 구성(시그니처 포함)
    public static DateResponseDTO.DateCourse createDateCourseInfo(List<DatePlace> datePlaces, String signature){
        List<DateResponseDTO.DatePlace> datePlaceDtos = datePlaces.stream()
                .map(DateConverter::createDatePlace)
                .toList();

        return DateResponseDTO.DateCourse.builder()
                .name(LocalDateTime.now().toLocalDate().toString())
                .datePlaces(datePlaceDtos)
                .signature(signature) // ← 추가
                .build();
    }

    // DatePlace -> DateResponseDTO.DatePlace
    public static DateResponseDTO.DatePlace createDatePlace(DatePlace datePlace) {
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
                .build();
    }

    // DateResponseDTO.DateCourse -> DateResponseDTO.DateCourse
    public static DateResponseDTO.DateCourse createDateCourse(DateCourse dateCourse){

        List<DateResponseDTO.DatePlace> datePlaces = dateCourse.getDatePlaceDateCourses().stream()
                .map(DatePlaceDateCourse::getDatePlace)
                .map(DateConverter::createDatePlace)
                .toList();

        return DateResponseDTO.DateCourse.builder()
                .name(dateCourse.getName())
                .datePlaces(datePlaces)
                .build();
    }

    // Page<DateCourse> -> DateRequestDTO.DateCourseList
    public static DateResponseDTO.DateCourseList createDateCourseList(Page<DateCourse> dateCourses){
        List<DateResponseDTO.DateCourse> dateCourseList = dateCourses.stream()
                .map(DateConverter::createDateCourse)
                .toList();

        return DateResponseDTO.DateCourseList.builder()
                .dateCourseList(dateCourseList)
                .totalPages(dateCourses.getTotalPages())
                .currentPage(dateCourses.getNumber())
                .currentSize(dateCourses.getSize())
                .hasNextPage(dateCourses.hasNext())
                .build();
    }

}
