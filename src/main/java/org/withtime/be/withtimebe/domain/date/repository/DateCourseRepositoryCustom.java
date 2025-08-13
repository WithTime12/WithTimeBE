package org.withtime.be.withtimebe.domain.date.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.date.dto.request.DateRequestDTO;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface DateCourseRepositoryCustom {
    public Page<DateCourse> searchDateCourseByApplyPage(DateRequestDTO.DateCourseSearchCond dateCourseSearchCond, Pageable pageable);
    public  Page<DateCourse> searchDateCourseBookmarkByMemberAndApplyPage(DateRequestDTO.DateCourseSearchCond dateCourseSearchCond, Member member, Pageable pageable);
}
