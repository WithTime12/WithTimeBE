package org.withtime.be.withtimebe.domain.date.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.date.entity.DateCourseBookmark;
import org.withtime.be.withtimebe.domain.member.entity.Member;

import java.util.Optional;

public interface DateCourseBookmarkRepository extends JpaRepository<DateCourseBookmark, Long> {
    Optional<DateCourseBookmark>  findByMemberAndDateCourse(Member member, DateCourse dateCourse);
}
