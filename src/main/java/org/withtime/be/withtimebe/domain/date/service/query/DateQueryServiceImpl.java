package org.withtime.be.withtimebe.domain.date.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.date.dto.request.DateRequestDTO;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.date.repository.DateCourseRepository;
import org.withtime.be.withtimebe.domain.log.placecategorylog.annotation.LogPlaceCategory;
import org.withtime.be.withtimebe.domain.member.annotation.GetPoint;
import org.withtime.be.withtimebe.domain.member.annotation.enums.PointAction;
import org.withtime.be.withtimebe.domain.member.entity.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DateQueryServiceImpl implements DateQueryService {

    private final DateCourseRepository dateCourseRepository;

    @LogPlaceCategory
    @GetPoint(action = PointAction.VIEW_DATE_COURSE)
    public Page<DateCourse> findDateCourses(DateRequestDTO.DateCourseSearchCond dateCourseSearchCond, Pageable pageable){
        return dateCourseRepository.searchDateCourseByApplyPage(dateCourseSearchCond, pageable);
    }

    @LogPlaceCategory
    @GetPoint(action = PointAction.VIEW_DATE_COURSE)
    public Page<DateCourse> findDateCourseBookmarks(DateRequestDTO.DateCourseSearchCond dateCourseSearchCond, Pageable pageable, Member member){
        return dateCourseRepository.searchDateCourseBookmarkByMemberAndApplyPage(dateCourseSearchCond, member, pageable);
    }
}
