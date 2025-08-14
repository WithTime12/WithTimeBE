package org.withtime.be.withtimebe.domain.date.service.command;

import org.withtime.be.withtimebe.domain.date.dto.request.DateRequestDTO;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.date.entity.DateCourseBookmark;
import org.withtime.be.withtimebe.domain.date.service.command.dto.RecommendedCourseResult;
import org.withtime.be.withtimebe.domain.member.entity.Member;


public interface DateCommandService {
    DateCourseBookmark createDateCourseBookmark(Long dateCourseId, Member member);
    DateCourse deleteDateCourseBookmark(Long dateCourseId, Member member);
    DateCourseBookmark createDateCourseBookmarkWithGeneratedCourse(DateRequestDTO.SaveDateCourse request, Member members);
    RecommendedCourseResult createDateCourse(DateRequestDTO.CreateDateCourse request);
}
