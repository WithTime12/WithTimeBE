package org.withtime.be.withtimebe.domain.date.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.withtime.be.withtimebe.domain.date.dto.request.DateRequestDTO;
import org.withtime.be.withtimebe.domain.date.entity.*;
import org.withtime.be.withtimebe.domain.date.entity.enums.DatePriceRange;
import org.withtime.be.withtimebe.domain.date.entity.enums.DateTime;
import org.withtime.be.withtimebe.domain.date.entity.enums.MealType;
import org.withtime.be.withtimebe.domain.date.entity.enums.Transportation;
import org.withtime.be.withtimebe.domain.member.entity.Member;

import java.util.Collection;
import java.util.List;

import static org.withtime.be.withtimebe.domain.date.entity.QDateCourse.dateCourse;


@RequiredArgsConstructor
@Repository
public class DateCourseRepositoryImpl implements DateCourseRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public Page<DateCourse> searchDateCourseByApplyPage(DateRequestDTO.DateCourseSearchCond dateCourseSearchCond, Pageable pageable) {
        QDateCourse dateCourse = QDateCourse.dateCourse;
        QDateCoursePlaceCategory dateCoursePlaceCategory = QDateCoursePlaceCategory.dateCoursePlaceCategory;
        QPlaceCategory placeCategory = QPlaceCategory.placeCategory;

        boolean hasKeywords = dateCourseSearchCond.userPreferredKeywords() != null && !dateCourseSearchCond.userPreferredKeywords().isEmpty();

        List<DateCourse> content = queryFactory.selectFrom(dateCourse)
                .where(datePriceRangeEq(dateCourseSearchCond.datePriceRange()),
                        datePlacesEq(dateCourseSearchCond.datePlaces()),
                        dateTimeEq(dateCourseSearchCond.dateDurationTime()),
                        mealTypesEq(dateCourseSearchCond.mealTypes()),
                        transportationEq(dateCourseSearchCond.transportation()),
                        hasKeywords ? existsAnyKeyword(dateCourse, dateCoursePlaceCategory,
                                placeCategory, dateCourseSearchCond.userPreferredKeywords()) : null
                        )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(dateCourse.id.countDistinct())
                .from(dateCourse)
                .where(datePriceRangeEq(dateCourseSearchCond.datePriceRange()),
                        datePlacesEq(dateCourseSearchCond.datePlaces()),
                        dateTimeEq(dateCourseSearchCond.dateDurationTime()),
                        mealTypesEq(dateCourseSearchCond.mealTypes()),
                        transportationEq(dateCourseSearchCond.transportation()),
                        hasKeywords ? existsAnyKeyword(dateCourse, dateCoursePlaceCategory,
                                placeCategory, dateCourseSearchCond.userPreferredKeywords()) : null
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    public  Page<DateCourse> searchDateCourseBookmarkByMemberAndApplyPage(DateRequestDTO.DateCourseSearchCond dateCourseSearchCond, Member member, Pageable pageable){
        QDateCourse dateCourse = QDateCourse.dateCourse;
        QDateCoursePlaceCategory dateCoursePlaceCategory = QDateCoursePlaceCategory.dateCoursePlaceCategory;
        QPlaceCategory placeCategory = QPlaceCategory.placeCategory;
        QDateCourseBookmark dateCourseBookmark = QDateCourseBookmark.dateCourseBookmark;

        boolean hasKeywords = dateCourseSearchCond.userPreferredKeywords() != null && !dateCourseSearchCond.userPreferredKeywords().isEmpty();

        List<DateCourse> content = queryFactory.selectFrom(dateCourse)
                .join(dateCourseBookmark).on(dateCourseBookmark.dateCourse.eq(dateCourse))
                .where(dateCourseBookmark.member.id.eq(member.getId()),
                        datePriceRangeEq(dateCourseSearchCond.datePriceRange()),
                        datePlacesEq(dateCourseSearchCond.datePlaces()),
                        dateTimeEq(dateCourseSearchCond.dateDurationTime()),
                        mealTypesEq(dateCourseSearchCond.mealTypes()),
                        transportationEq(dateCourseSearchCond.transportation()),
                        hasKeywords ? existsAnyKeyword(dateCourse, dateCoursePlaceCategory,
                                placeCategory, dateCourseSearchCond.userPreferredKeywords()) : null
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(dateCourse.id.countDistinct())
                .from(dateCourse)
                .join(dateCourseBookmark).on(dateCourseBookmark.dateCourse.eq(dateCourse))
                .where(dateCourseBookmark.member.id.eq(member.getId()),
                        datePriceRangeEq(dateCourseSearchCond.datePriceRange()),
                        datePlacesEq(dateCourseSearchCond.datePlaces()),
                        dateTimeEq(dateCourseSearchCond.dateDurationTime()),
                        mealTypesEq(dateCourseSearchCond.mealTypes()),
                        transportationEq(dateCourseSearchCond.transportation()),
                        hasKeywords ? existsAnyKeyword(dateCourse, dateCoursePlaceCategory,
                                placeCategory, dateCourseSearchCond.userPreferredKeywords()) : null
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private BooleanExpression existsAnyKeyword(
        QDateCourse dateCourse,
        QDateCoursePlaceCategory dcpc,
        QPlaceCategory placeCategory,
        Collection<String> labels
    ){
        return JPAExpressions.selectOne()
                .from(dcpc)
                .join(placeCategory).on(placeCategory.eq(dcpc.placeCategory))
                .where(
                        dcpc.dateCourse.eq(dateCourse),
                        placeCategory.label.in(labels)
                ).exists();
    }

    private BooleanExpression datePriceRangeEq(DatePriceRange datePriceRange){
        return datePriceRange == null ? null: dateCourse.datePriceRange.eq(datePriceRange);
    }

    private BooleanExpression datePlacesEq(List<String> datePlaces){
        return datePlaces == null ? null : dateCourse.datePlaces.any().in(datePlaces);
    }

    private BooleanExpression dateTimeEq(DateTime dateTime){
        return dateTime == null ? null : dateCourse.dateTime.eq(dateTime);
    }

    private BooleanExpression mealTypesEq(List<MealType> mealTypes){
        return mealTypes== null ? null : dateCourse.mealTypes.any().in(mealTypes);
    }

    private BooleanExpression transportationEq(Transportation transportation){
        return transportation == null ? null : dateCourse.transportation.eq(transportation);
    }

    private BooleanExpression allEq(DatePriceRange datePriceRange, List<String> datePlaces,
                                    DateTime dateTime, List<MealType> mealTypes, Transportation transportation
    ){
        return datePriceRangeEq(datePriceRange).and(datePlacesEq(datePlaces))
                .and(dateTimeEq(dateTime)).and(mealTypesEq(mealTypes))
                .and(transportationEq(transportation));
    }

}
