package org.withtime.be.withtimebe.domain.date.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.date.entity.DateCourseBookmark;
import org.withtime.be.withtimebe.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface DateCourseBookmarkRepository extends JpaRepository<DateCourseBookmark, Long> {
    Optional<DateCourseBookmark> findByMemberAndDateCourse(Member member, DateCourse dateCourse);

    @Query("""
      select b.dateCourse.id
      from DateCourseBookmark b
      where b.member.id = :memberId
        and b.dateCourse.id in :courseIds
    """)
    List<Long> findBookmarkedCourseIds(Long memberId, List<Long> courseIds);
}
