package org.withtime.be.withtimebe.domain.date.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;

public interface DateCourseRepository extends JpaRepository<DateCourse, Long>, DateCourseRepositoryCustom {
}
