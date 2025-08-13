package org.withtime.be.withtimebe.domain.date.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.date.converter.DateConverter;
import org.withtime.be.withtimebe.domain.date.dto.request.DateRequestDTO;
import org.withtime.be.withtimebe.domain.date.entity.DateCourse;
import org.withtime.be.withtimebe.domain.date.entity.DateCourseBookmark;
import org.withtime.be.withtimebe.domain.date.entity.DatePlace;
import org.withtime.be.withtimebe.domain.date.entity.DatePlaceDateCourse;
import org.withtime.be.withtimebe.domain.date.entity.enums.BudgetLevel;
import org.withtime.be.withtimebe.domain.date.entity.enums.KeywordForBudget;
import org.withtime.be.withtimebe.domain.date.entity.enums.MealType;
import org.withtime.be.withtimebe.domain.date.entity.enums.PlaceType;
import org.withtime.be.withtimebe.domain.date.entity.model.ScheduledDateCourse;
import org.withtime.be.withtimebe.domain.date.entity.model.ScheduledDatePlace;
import org.withtime.be.withtimebe.domain.date.repository.DateCourseBookmarkRepository;
import org.withtime.be.withtimebe.domain.date.repository.DateCourseRepository;
import org.withtime.be.withtimebe.domain.date.repository.DatePlaceRepository;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.global.error.code.DateCourseErrorCode;
import org.withtime.be.withtimebe.global.error.exception.DateCourseException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Transactional
public class DateCommandServiceImpl implements DateCommandService{

    private final DateCourseBookmarkRepository dateCourseBookmarkRepository;
    private final DateCourseRepository dateCourseRepository;
    private final DatePlaceRepository datePlaceRepository;

    // 사용자 맞춤형 데이트 코스 생성
    public List<DatePlace> createDateCourse(
            DateRequestDTO.CreateDateCourse request
    ) {
        // 데이트 장소 장소로 필터링
        List<DatePlace> datePlaces = new ArrayList<>();
        for (String datePlace : request.datePlaces()) {
            String[] dateKeywords = datePlace.split(" ");
            String placeKeyword1 = dateKeywords[0];
            String placeKeyword2 = dateKeywords[1];
            datePlaces = datePlaceRepository.findByAddressContainingAll(placeKeyword1,  placeKeyword2);
        }
        int placeCountByTime = request.dateDurationTime().getValue();
        Optional<MealType> mealType = MealType.getMealTypeByTime(request.startTime());
        List<ScheduledDateCourse> scheduledDateCourses = switch (request.budget()) {
            case UNDER_10K ->
                // 패턴 만들어줌
                    getScheduledDateCourses(
                            request, mealType, placeCountByTime, datePlaces, BudgetLevel.FREE, request.mealPlan());
            case FROM_10K_TO_20K -> getScheduledDateCourses(
                    request, mealType, placeCountByTime, datePlaces, BudgetLevel.LOW, request.mealPlan());
            case FROM_20K_TO_30K -> getScheduledDateCourses(
                    request, mealType, placeCountByTime, datePlaces, BudgetLevel.MEDIUM, request.mealPlan());
            case OVER_30K -> getScheduledDateCourses(
                    request, mealType, placeCountByTime, datePlaces, BudgetLevel.HIGH, request.mealPlan());
        };

        List<List<DatePlace>> courses = scheduledDateCourses.stream()
                .sorted()
                .map(dateCourse -> dateCourse.getScheduledDatePlaces().stream()
                        .map(ScheduledDatePlace::getDatePlace).toList())
                .limit(4)
                .toList();

        if (request.attemptCount() == 3){
            DateCourse dateCourse = DateCourse.builder().build();
            for (List<DatePlace> datePlacesTop3 : courses) {
                List<DatePlaceDateCourse> datePlaceDateCourses = datePlacesTop3.stream()
                        .map(datePlace -> DatePlaceDateCourse.builder()
                                .datePlace(datePlace).build())
                        .toList();
                assignScheduleTimes(datePlaceDateCourses, request.startTime());
                dateCourse.addDatePlaceDateCourses(datePlaceDateCourses);
                dateCourseRepository.save(dateCourse);
            }
        }
        return !courses.isEmpty() ? courses.get(request.attemptCount()) : new ArrayList<>();
    }

    private void assignScheduleTimes(List<DatePlaceDateCourse> datePlaceDateCourse, LocalDateTime startedAt){
        LocalDateTime cursor = startedAt;

        for (DatePlaceDateCourse placeDateCourse : datePlaceDateCourse) {
            placeDateCourse.setStartTime(cursor);
            PlaceType placeType = placeDateCourse.getDatePlace().getPlaceType();
            Duration duration = placeType.getDuration();

            LocalDateTime end = cursor.plus(duration);
            placeDateCourse.setEndTime(end);

            cursor = end;
        }
    }

    private List<ScheduledDateCourse> getScheduledDateCourses(
            DateRequestDTO.CreateDateCourse request, Optional<MealType> mealType,
            int placeCountByTime, List<DatePlace> datePlaces, BudgetLevel budgetLevel, List<MealType> mealPlan
    ) {
        List<PlaceType> patterns = buildPattern(mealType, request.startTime(), placeCountByTime, mealPlan);
        // 예산 키워드
        List<String> keywordsForBudget = KeywordForBudget.getKeywordsByBudget(budgetLevel).stream()
                .map(KeywordForBudget::getLabel)
                .toList();
        // 사용자 맞춤 & 예산 키워드 맞춤 장소 가중치 계산
        List<ScheduledDatePlace> scheduledDatePlaces = scorePlacesDto(datePlaces, request.userPreferredKeywords(), keywordsForBudget);
        // 위에 있는 패턴과 장소로 조합을 만들어서 코스 반환(조합)
        return generateCombination(patterns, scheduledDatePlaces);
    }

    // 데이트코스 북마크 생성 - 직접 데이트 코스 찾아보기
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

    // 데이트코스 북마크 생성 - AI 기반 데이트 코스 만들기
    public DateCourseBookmark createDateCourseBookmarkWithGeneratedCourse(
            DateRequestDTO.SaveDateCourse request,
            Member member
    ){
        DateCourse dateCourse = DateConverter.createDateCourse(request);
        List<DatePlace> datePlaces = datePlaceRepository.findAllById(request.datePlaceIds());
        List<DatePlaceDateCourse> datePlaceDateCourses = datePlaces.stream()
                .map(datePlace -> DatePlaceDateCourse.builder().datePlace(datePlace).build())
                .toList();
        dateCourse.addDatePlaceDateCourses(datePlaceDateCourses);
        dateCourseRepository.save(dateCourse);

        DateCourseBookmark dateCourseBookmark = DateConverter.createDateCourseBookmark(dateCourse, member);
        return dateCourseBookmarkRepository.save(dateCourseBookmark);
    }

    private List<PlaceType> buildPattern(Optional<MealType> mealType, LocalDateTime startTime
            , int placeCountByTime, List<MealType> mealPlan) {
        List<PlaceType> pattern = new ArrayList<>();
        while (pattern.size() < placeCountByTime) {
            if (mealType.isPresent() && mealPlan.contains(mealType.get())) {
                pattern.add(PlaceType.TIME_EAT);
            } else {
                if (pattern.size() + 2 <= placeCountByTime) {
                    pattern.add(PlaceType.TIME_SEE);
                    pattern.add(PlaceType.TIME_CAFE);
                } else {
                    pattern.add(PlaceType.TIME_SEE);
                }
            }
        }
        return pattern;
    }

    private List<ScheduledDatePlace> scorePlacesDto(
            List<DatePlace> datePlaces,
            List<String> userPreferredKeywords,
            List<String> budgetKeywords
    ){
        return datePlaces.stream()
                .map(place ->
                        {
                            List<String> labels = place.getPlaceCategories().stream()
                                    .map(dp -> dp.getPlaceCategory().getLabel())
                                    .toList();
                            double score = 0.0;

                            for (String label : labels) {
                                if (budgetKeywords.contains(label)){
                                    score += 1.0;
                                }
                            }

                            for (String userPreferredKeyword : userPreferredKeywords) {
                                if (labels.contains(userPreferredKeyword)){
                                    score += 1.0;
                                }
                            }

                            return ScheduledDatePlace.builder()
                                    .datePlace(place)
                                    .score(score)
                                    .build();
                        })
                .sorted(Comparator.comparingDouble(ScheduledDatePlace::getScore).reversed())
                .toList();
    }

    // 조합 생성
    private List<ScheduledDateCourse> generateCombination(List<PlaceType> pattern, List<ScheduledDatePlace> datePlaces){
        List<List<ScheduledDatePlace>> result = new ArrayList<>();
        generateByRecur(pattern, datePlaces, 0, new ArrayList<>(), result);
        return result.stream()
                .map(list -> {
                    double score = list.stream()
                            .mapToDouble(ScheduledDatePlace::getScore)
                            .sum();
                    return ScheduledDateCourse.builder()
                            .scheduledDatePlaces(list)
                            .weight(score)
                            .build();
                })
                .toList();
    }

    private void generateByRecur(
            List<PlaceType> pattern,
            List<ScheduledDatePlace> allCandidates,
            int index,
            List<ScheduledDatePlace> current,
            List<List<ScheduledDatePlace>> result
    ){
        if (index == pattern.size()){
            result.add(new ArrayList<>(current));
            return;
        }
        PlaceType neededType = pattern.get(index);

        for (ScheduledDatePlace candidate : allCandidates) {
            if (candidate.getDatePlace().getPlaceType() != neededType) continue;
            if (current.contains(candidate)) continue;

            current.add(candidate);
            generateByRecur(pattern, allCandidates, index + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}
