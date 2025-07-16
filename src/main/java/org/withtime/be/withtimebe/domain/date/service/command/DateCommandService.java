package org.withtime.be.withtimebe.domain.date.service.command;

import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.date.entity.DateCourseBookmark;
import org.withtime.be.withtimebe.domain.member.entity.Member;


public interface DateCommandService {
    public DateCourseBookmark createDateCourseBookmark(Long dateCourseId, Member member);
    public DateCourse deleteDateCourseBookmark(Long dateCourseId, Member member);
}
