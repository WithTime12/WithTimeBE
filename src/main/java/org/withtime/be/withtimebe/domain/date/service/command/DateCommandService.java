package org.withtime.be.withtimebe.domain.date.service.command;

import org.withtime.be.withtimebe.domain.date.dto.request.DateRequestDTO;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.date.entity.DateCourseBookmark;
import org.withtime.be.withtimebe.domain.date.entity.DatePlace;
import org.withtime.be.withtimebe.domain.member.entity.Member;

import java.util.List;


public interface DateCommandService {
    public DateCourseBookmark createDateCourseBookmark(Long dateCourseId, Member member);
    public DateCourse deleteDateCourseBookmark(Long dateCourseId, Member member);
    public DateCourseBookmark createDateCourseBookmarkWithGeneratedCourse(DateRequestDTO.SaveDateCourse request, Member members);
    public List<DatePlace> createDateCourse(DateRequestDTO.CreateDateCourse request);
}
