package org.withtime.be.withtimebe.domain.date.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.date.converter.DateConverter;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.date.entity.DateCourseBookmark;
import org.withtime.be.withtimebe.domain.date.repository.DateCourseBookmarkRepository;
import org.withtime.be.withtimebe.domain.date.repository.DateCourseRepository;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;
import org.withtime.be.withtimebe.global.error.code.DateCourseErrorCode;
import org.withtime.be.withtimebe.global.error.exception.DateCourseException;


@RequiredArgsConstructor
@Service
public class DateCommandServiceImpl implements DateCommandService{

    private final DateCourseBookmarkRepository dateCourseBookmarkRepository;
    private final DateCourseRepository dateCourseRepository;
    private final MemberRepository memberRepository;



    // 데이트코스 북마크 생성
    public DateCourseBookmark createDateCourseBookmark(Long dateCourseId, Member member) {
        DateCourse dateCourse = dateCourseRepository.findById(dateCourseId)
                .orElseThrow(() -> new DateCourseException(DateCourseErrorCode.DateCourse_NOT_FOUND));
        DateCourseBookmark dateCourseBookmark = DateConverter.createDateCourseBookmark(dateCourse, member);
        return dateCourseBookmarkRepository.save(dateCourseBookmark);
    }

    // 데이트코스 북마크 삭제
    public DateCourse deleteDateCourseBookmark(Long dateCourseId, Member member){
        DateCourse dateCourse = dateCourseRepository.findById(dateCourseId)
                .orElseThrow(() -> new DateCourseException(DateCourseErrorCode.DateCourse_NOT_FOUND));
        DateCourseBookmark dateCourseBookmark = dateCourseBookmarkRepository.findByMemberAndDateCourse(member, dateCourse)
                .orElseThrow(() -> new DateCourseException(DateCourseErrorCode.DateCourseBookMark_NOT_FOUND));
        dateCourseBookmarkRepository.delete(dateCourseBookmark);
        return dateCourse;
    }


}
