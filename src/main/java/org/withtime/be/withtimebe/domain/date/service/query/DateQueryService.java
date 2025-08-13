package org.withtime.be.withtimebe.domain.date.service.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.date.dto.request.DateRequestDTO;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface DateQueryService {
    public Page<DateCourse> findDateCourses(DateRequestDTO.DateCourseSearchCond dateCourseSearchCond, Pageable pageable);
    public Page<DateCourse> findDateCourseBookmarks(DateRequestDTO.DateCourseSearchCond dateCourseSearchCond, Pageable pageable, Member member);
}
