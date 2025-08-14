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
import org.withtime.be.withtimebe.domain.date.service.command.dto.RecommendedCourseResult;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.global.error.code.DateCourseErrorCode;
import org.withtime.be.withtimebe.global.error.exception.DateCourseException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class DateCommandServiceImpl implements DateCommandService{

    private final DateCourseBookmarkRepository dateCourseBookmarkRepository;
    private final DateCourseRepository dateCourseRepository;
    private final DatePlaceRepository datePlaceRepository;

    @Transactional(readOnly = true)
    /** 단일 코스 생성 (저장/북마크/attemptCount 없음, excludedCourseSignatures로 중복 제외) */
    public RecommendedCourseResult createDateCourse(DateRequestDTO.CreateDateCourse request) {
        if (request == null || request.dateDurationTime() == null) {
            return new RecommendedCourseResult(List.of(), null);
        }

        // 1) 후보 장소 수집 (주소 토큰 기반) + ID 기준 중복 제거(순서 보존)
        List<DatePlace> candidates = collectCandidatesByAddressTokens(request.datePlaces());
        if (candidates.isEmpty()) return new RecommendedCourseResult(List.of(), null);

        // 2) 코스 길이/식사/예산 레벨 산정
        int courseCount = request.dateDurationTime().getValue();
        Optional<MealType> mealType = (request.startTime() == null)
                ? Optional.empty()
                : MealType.getMealTypeByTime(request.startTime());

        BudgetLevel budgetLevel = switch (request.budget()) {
            case UNDER_10K -> BudgetLevel.FREE;
            case FROM_10K_TO_20K -> BudgetLevel.LOW;
            case FROM_20K_TO_30K -> BudgetLevel.MEDIUM;
            case OVER_30K -> BudgetLevel.HIGH;
            default -> BudgetLevel.LOW;
        };

        // 3) place_type 패턴 결정 (정책 반영)
        List<PlaceType> pattern = buildPattern(mealType, request.startTime(), courseCount, request.mealPlan(), budgetLevel);

        // 4) 스코어링 (사용자/예산 키워드 일치 시 +1)
        Set<String> budgetKw = KeywordForBudget.getKeywordsByBudget(budgetLevel).stream()
                .map(KeywordForBudget::getLabel)
                .collect(Collectors.toSet());
        Set<String> userKw = (request.userPreferredKeywords() == null)
                ? Collections.emptySet()
                : new HashSet<>(request.userPreferredKeywords());

        List<ScheduledDatePlace> scored = scorePlacesDto(candidates, userKw, budgetKw);

        // (선택) 타입별 상위 N만 남겨 조합 폭발 방지 — 필요시 주석 해제
        // final int TOP_N = 10;
        // Map<PlaceType, List<ScheduledDatePlace>> byType = scored.stream()
        //         .collect(Collectors.groupingBy(sp -> sp.getDatePlace().getPlaceType()));
        // List<ScheduledDatePlace> trimmed = byType.values().stream()
        //         .flatMap(list -> list.stream().limit(TOP_N))
        //         .toList();
        // List<ScheduledDateCourse> combos = new ArrayList<>(generateCombination(pattern, trimmed));

        // 5) 모든 조합 생성 → 동점 무작위화 후 점수 내림차순 정렬
        List<ScheduledDateCourse> combos = new ArrayList<>(generateCombination(pattern, scored));
        if (combos.isEmpty()) return new RecommendedCourseResult(List.of(), null);
        Collections.shuffle(combos); // 동점 집합 무작위 섞기
        combos.sort(ScheduledDateCourse.BY_WEIGHT_DESC);

        // 6) 이미 본 코스(시그니처) 제외하고 첫 코스 반환
        Set<String> excluded = (request.excludedCourseSignatures() == null)
                ? Collections.emptySet()
                : new HashSet<>(request.excludedCourseSignatures());

        for (ScheduledDateCourse c : combos) {
            String sig = courseSignature(c.getScheduledDatePlaces());
            if (!excluded.contains(sig)) {
                List<DatePlace> places = c.getScheduledDatePlaces().stream()
                        .map(ScheduledDatePlace::getDatePlace)
                        .toList();
                return new RecommendedCourseResult(places, sig);
            }
        }

        // 전부 이미 본 코스면, 최고점 코스라도 반환
        ScheduledDateCourse top = combos.get(0);
        String sig = courseSignature(top.getScheduledDatePlaces());
        List<DatePlace> places = top.getScheduledDatePlaces().stream()
                .map(ScheduledDatePlace::getDatePlace)
                .toList();
        return new RecommendedCourseResult(places, sig);
    }

    // ───────────────────────── 내부 로직 ─────────────────────────
    /** 주소 키워드 토큰으로 후보 조회 + ID 기준 중복 제거(순서 보존) */
    private List<DatePlace> collectCandidatesByAddressTokens(List<String> tokens) {
        if (tokens == null || tokens.isEmpty()) return List.of();

        Map<Long, DatePlace> byId = new LinkedHashMap<>();
        // id 없는 엔티티 중복 방지(레퍼런스 기준)
        Set<DatePlace> noIdSet = new LinkedHashSet<>();

        for (String token : tokens) {
            if (token == null || token.isBlank()) continue;
            String[] parts = token.trim().split("\\s+");
            String k1 = parts.length >= 1 ? parts[0] : "";
            String k2 = parts.length >= 2 ? parts[1] : "";
            List<DatePlace> found = datePlaceRepository.findByAddressContainingAll(k1, k2);
            for (DatePlace p : found) {
                Long id = p.getId();
                if (id != null) {
                    byId.putIfAbsent(id, p);
                } else {
                    noIdSet.add(p);
                }
            }
        }
        List<DatePlace> result = new ArrayList<>(byId.values());
        result.addAll(noIdSet);
        return result;
    }

    /** 정책 기반 place_type 분배 (유효성 검사는 제외) */
    private List<PlaceType> buildPattern(
            Optional<MealType> mealType, LocalDateTime startTime,
            int courseCount, List<MealType> mealPlan, BudgetLevel budgetLevel
    ) {
        int eat = 0, see = 0, cafe = 0;
        switch (courseCount) {
            case 2 -> { see = 1; cafe = 1; }
            case 3 -> { eat = 1; see = 1; cafe = 1; }
            case 4 -> { eat = 1; see = 2; cafe = 1; }
            default -> { eat = 1; see = Math.max(1, courseCount - 2); cafe = 1; }
        }
        // 저예산 → 구경 위주
        if (budgetLevel == BudgetLevel.FREE || budgetLevel == BudgetLevel.LOW) {
            eat = Math.min(eat, 1);
            see = Math.max(1, courseCount - eat - 1);
            cafe = courseCount - eat - see;
        }
        // 시작 시간의 식사타입이 mealPlan에 없으면 식사 제외
        if (mealType.isEmpty() || mealPlan == null || !mealPlan.contains(mealType.get())) {
            eat = 0;
            see = Math.max(1, courseCount - 1);
            cafe = courseCount - see;
        }

        List<PlaceType> pattern = new ArrayList<>();
        if (eat > 0) { pattern.add(PlaceType.TIME_EAT); eat--; }
        while (see > 0 || cafe > 0) {
            if (see > 0) { pattern.add(PlaceType.TIME_SEE); see--; }
            if (cafe > 0) { pattern.add(PlaceType.TIME_CAFE); cafe--; }
        }
        return pattern;
    }

    /** 점수 계산(사용자 + 예산 키워드 모두 +1) — null-safe */
    private List<ScheduledDatePlace> scorePlacesDto(
            List<DatePlace> datePlaces,
            Set<String> userPreferredKeywords,
            Set<String> budgetKeywords
    ) {
        List<DatePlace> safePlaces = (datePlaces == null) ? List.of() : datePlaces;
        Set<String> userKws = (userPreferredKeywords == null) ? Set.of() : userPreferredKeywords;
        Set<String> budgetKws = (budgetKeywords == null) ? Set.of() : budgetKeywords;

        return safePlaces.stream()
                .map(place -> {
                    List<String> labels =
                            (place.getPlaceCategories() == null) ? List.of()
                                    : place.getPlaceCategories().stream()
                                    .map(dp -> dp.getPlaceCategory())
                                    .filter(Objects::nonNull)
                                    .map(pc -> pc.getLabel())
                                    .filter(Objects::nonNull)
                                    .toList();

                    double score = 0.0;
                    if (!labels.isEmpty()) {
                        if (!budgetKws.isEmpty()) {
                            for (String label : labels) {
                                if (budgetKws.contains(label)) score += 1.0;
                            }
                        }
                        if (!userKws.isEmpty()) {
                            for (String kw : userKws) {
                                if (labels.contains(kw)) score += 1.0;
                            }
                        }
                    }

                    return ScheduledDatePlace.ofScoreOnly(place, score);
                })
                .sorted(Comparator.comparingDouble(ScheduledDatePlace::getScore).reversed())
                .toList();
    }

    /** 패턴 순서에 맞춰 모든 조합 생성 */
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
            if (current.contains(candidate)) continue; // 동일 장소 중복 방지

            current.add(candidate);
            generateByRecur(pattern, allCandidates, index + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    /** 코스 시그니처(장소ID 순서대로, ID 없으면 name 대체) */
    private String courseSignature(List<ScheduledDatePlace> list) {
        return list.stream()
                .map(s -> {
                    DatePlace p = s.getDatePlace();
                    Long id = (p != null) ? p.getId() : null;
                    if (id != null) return String.valueOf(id);
                    String name = (p != null) ? p.getName() : "unknown";
                    return name != null ? name : "unknown";
                })
                .collect(Collectors.joining("-"));
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

    // 데이트코스 북마크 생성 - 직접 데이트 코스 찾아보기
    public DateCourseBookmark createDateCourseBookmark(Long dateCourseId, Member member) {
        DateCourse dateCourse = dateCourseRepository.findById(dateCourseId)
                .orElseThrow(() -> new DateCourseException(DateCourseErrorCode.DateCourse_NOT_FOUND));
        DateCourseBookmark dateCourseBookmark = DateConverter.createDateCourseBookmark(dateCourse, member);
        return dateCourseBookmarkRepository.save(dateCourseBookmark);
    }
}
