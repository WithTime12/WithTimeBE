package org.withtime.be.withtimebe.domain.date.converter;

import org.withtime.be.withtimebe.domain.date.dto.response.DateResponseDTO;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.date.entity.DateCourseBookmark;
import org.withtime.be.withtimebe.domain.member.entity.Member;

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


}
