package org.withtime.be.withtimebe.domain.weather.data.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.weather.converter.WeatherConverter;
import org.withtime.be.withtimebe.domain.weather.converter.WeatherSyncConverter;
import org.withtime.be.withtimebe.domain.weather.data.utils.WeatherClassificationUtils;
import org.withtime.be.withtimebe.domain.weather.data.utils.WeatherDataHelper;
import org.withtime.be.withtimebe.domain.weather.data.utils.WeatherRecommendationUtils;
import org.withtime.be.withtimebe.domain.weather.dto.request.WeatherReqDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherResDTO;
import org.withtime.be.withtimebe.domain.weather.dto.response.WeatherSyncResDTO;
import org.withtime.be.withtimebe.domain.weather.entity.*;
import org.withtime.be.withtimebe.domain.weather.entity.enums.WeatherType;
import org.withtime.be.withtimebe.domain.weather.repository.*;
import org.withtime.be.withtimebe.global.error.code.WeatherErrorCode;
import org.withtime.be.withtimebe.global.error.exception.WeatherException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherRecommendationGenerationServiceImpl implements WeatherRecommendationGenerationService {

    private final RegionRepository regionRepository;
    private final RawShortTermWeatherRepository shortTermWeatherRepository;
    private final RawMediumTermWeatherRepository mediumTermWeatherRepository;
    private final WeatherTemplateRepository weatherTemplateRepository;
    private final DailyRecommendationRepository dailyRecommendationRepository;
    private final WeatherClassificationService classificationService;

    @Override
    @Transactional
    public WeatherSyncResDTO.RecommendationGenerationResult generateRecommendations(
            List<Long> regionIds, LocalDate startDate, LocalDate endDate,
            boolean forceRegenerate, String recommendationType) {

        LocalDateTime startTime = LocalDateTime.now();
        log.info("{} 추천 정보 생성 시작: regionIds={}, startDate={}, endDate={}, forceRegenerate={}",
                recommendationType, regionIds, startDate, endDate, forceRegenerate);

        List<Region> targetRegions = WeatherDataHelper.getTargetRegions(regionIds, regionRepository);
        List<WeatherSyncResDTO.RegionRecommendationResult> regionResults = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        Map<WeatherType, Integer> weatherStats = new HashMap<>();

        Map<String, WeatherTemplate> templateMap = WeatherRecommendationUtils.createTemplateMap(weatherTemplateRepository.findAllWithKeywords());

        for (Region region : targetRegions) {
            long regionStartTime = System.currentTimeMillis();
            try {
                RegionRecommendationResult result = generateRecommendationsForRegion(
                        region, startDate, endDate, forceRegenerate, templateMap, recommendationType);

                updateStats(regionResults, weatherStats, region, result, regionStartTime);

            } catch (Exception e) {
                handleError(regionResults, errorMessages, region, e, regionStartTime, recommendationType);
            }
        }

        LocalDateTime endTime = LocalDateTime.now();
        log.info("{} 추천 정보 생성 완료: 성공 {}/{} 지역, 신규 {}, 업데이트 {} 추천, 처리시간 {}ms",
                recommendationType,
                regionResults.stream().filter(WeatherSyncResDTO.RegionRecommendationResult::success).count(),
                targetRegions.size(),
                regionResults.stream().mapToInt(WeatherSyncResDTO.RegionRecommendationResult::newRecommendations).sum(),
                regionResults.stream().mapToInt(WeatherSyncResDTO.RegionRecommendationResult::updatedRecommendations).sum(),
                ChronoUnit.MILLIS.between(startTime, endTime));

        return WeatherSyncConverter.toRecommendationGenerationResult(
                targetRegions.size(),
                (int) regionResults.stream().filter(WeatherSyncResDTO.RegionRecommendationResult::success).count(),
                (int) regionResults.stream().filter(r -> !r.success()).count(),
                regionResults.stream().mapToInt(WeatherSyncResDTO.RegionRecommendationResult::recommendationsGenerated).sum(),
                regionResults.stream().mapToInt(WeatherSyncResDTO.RegionRecommendationResult::newRecommendations).sum(),
                regionResults.stream().mapToInt(WeatherSyncResDTO.RegionRecommendationResult::updatedRecommendations).sum(),
                startDate, endDate, startTime, endTime, regionResults, weatherStats, errorMessages);
    }

    @Override
    public WeatherResDTO.WeeklyRecommendation getWeeklyRecommendation(
            WeatherReqDTO.GetWeeklyRecommendation request) {
        log.info("주간 날씨 추천 조회 요청: regionId={}, startDate={}",
                request.regionId(), request.startDate());

        // 1. 지역 존재 확인
        Region region = validateRegionExists(request.regionId());

        // 2. 주간 추천 정보 조회
        LocalDate endDate = request.getEndDate();
        List<DailyRecommendation> recommendations =
                dailyRecommendationRepository.findWeeklyRecommendations(
                        request.regionId(), request.startDate(), endDate.plusDays(1));

        log.info("주간 날씨 추천 조회 완료: regionId={}, 조회된 데이터 수={}",
                request.regionId(), recommendations.size());

        return WeatherConverter.toWeeklyRecommendation(
                recommendations, region.getId(), region.getName(),
                request.startDate(), endDate);
    }

    @Override
    public WeatherResDTO.WeeklyPrecipitation getWeeklyPrecipitation(
            WeatherReqDTO.GetWeeklyPrecipitation request) {

        log.info("주간 강수확률 조회 요청: regionId={}, startDate={}",
                request.regionId(), request.startDate());

        // 1. 지역 존재 확인
        Region region = validateRegionExists(request.regionId());
        LocalDate endDate = request.getEndDate();

        // 2. 7일치 데이터 한 번에 조회
        List<RawMediumTermWeather> mediumTermDataList =
                mediumTermWeatherRepository.findByRegionIdAndForecastDateRange(
                        request.regionId(), request.startDate(), endDate);

        List<RawShortTermWeather> shortTermDataList =
                shortTermWeatherRepository.findByRegionIdAndForecastDateRange(
                        request.regionId(), request.startDate(), endDate);

        // 3. 날짜별로 그룹핑 (메모리에서 처리)
        Map<LocalDate, List<RawMediumTermWeather>> mediumTermByDate = mediumTermDataList.stream()
                .collect(Collectors.groupingBy(RawMediumTermWeather::getForecastDate));

        Map<LocalDate, List<RawShortTermWeather>> shortTermByDate = shortTermDataList.stream()
                .collect(Collectors.groupingBy(RawShortTermWeather::getForecastDate));

        // 4. 7일간 강수확률 정보 구성
        List<WeatherResDTO.DailyPrecipitation> dailyPrecipitations = new ArrayList<>();

        for (LocalDate date = request.startDate(); !date.isAfter(endDate); date = date.plusDays(1)) {
            WeatherResDTO.DailyPrecipitation dailyPrecip = getPrecipitationForDateOptimized(
                    date, mediumTermByDate.get(date), shortTermByDate.get(date));
            dailyPrecipitations.add(dailyPrecip);
        }

        log.info("주간 강수확률 조회 완료: regionId={}, 조회된 데이터 수={}, 중기예보 {}건, 단기예보 {}건",
                request.regionId(), dailyPrecipitations.size(),
                mediumTermDataList.size(), shortTermDataList.size());

        return WeatherResDTO.WeeklyPrecipitation.builder()
                .region(WeatherConverter.toRegionInfo(region))
                .startDate(request.startDate())
                .endDate(endDate)
                .dailyPrecipitations(dailyPrecipitations)
                .totalDays(dailyPrecipitations.size())
                .message(String.format("%s 지역의 %s부터 %s까지 7일간 강수확률 정보입니다.",
                        region.getName(), request.startDate(), endDate))
                .build();
    }

    private void updateStats(List<WeatherSyncResDTO.RegionRecommendationResult> regionResults,
                             Map<WeatherType, Integer> weatherStats,
                             Region region, RegionRecommendationResult result, long startTime) {

        WeatherRecommendationUtils.mergeWeatherStats(weatherStats, result.weatherTypeStats());

        regionResults.add(WeatherSyncResDTO.RegionRecommendationResult.builder()
                .regionId(region.getId())
                .regionName(region.getName())
                .success(true)
                .recommendationsGenerated(result.recommendationsGenerated())
                .newRecommendations(result.newRecommendations())
                .updatedRecommendations(result.updatedRecommendations())
                .processedDates(result.processedDates())
                .errorMessage(null)
                .processingTimeMs(System.currentTimeMillis() - startTime)
                .build());
    }

    private void handleError(List<WeatherSyncResDTO.RegionRecommendationResult> regionResults,
                             List<String> errorMessages,
                             Region region, Exception e, long startTime, String type) {
        long timeTaken = System.currentTimeMillis() - startTime;
        String msg = String.format("지역 %s 추천 생성 실패: %s", region.getName(), e.getMessage());
        log.error("{} 추천 생성: 지역 {} 실패", type, region.getName(), e);
        errorMessages.add(msg);
        regionResults.add(WeatherSyncResDTO.RegionRecommendationResult.builder()
                .regionId(region.getId())
                .regionName(region.getName())
                .success(false)
                .recommendationsGenerated(0)
                .newRecommendations(0)
                .updatedRecommendations(0)
                .processedDates(Collections.emptyList())
                .errorMessage(msg)
                .processingTimeMs(timeTaken)
                .build());
    }

    private RegionRecommendationResult generateRecommendationsForRegion(
            Region region, LocalDate startDate, LocalDate endDate,
            boolean forceRegenerate, Map<String, WeatherTemplate> templateMap, String type) {

        List<String> processedDates = new ArrayList<>();
        Map<WeatherType, Integer> stats = new HashMap<>();
        int generated = 0, created = 0, updated = 0;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            try {
                RecommendationResult result = generateRecommendationForDate(region, date, forceRegenerate, templateMap);
                if (result != null) {
                    generated++;
                    if (result.isNew()) created++; else updated++;
                    processedDates.add(date.toString());
                    stats.merge(result.weatherType(), 1, Integer::sum);
                }
            } catch (Exception e) {
                log.warn("{} 추천 생성 실패: {} {}", type, region.getName(), date);
            }
        }

        return new RegionRecommendationResult(generated, created, updated, processedDates, stats);
    }

    private RecommendationResult generateRecommendationForDate(Region region, LocalDate date,
                                                               boolean forceRegenerate, Map<String, WeatherTemplate> templateMap) {

        Optional<DailyRecommendation> existing = dailyRecommendationRepository.findByRegionIdAndDateWithTemplate(region.getId(), date);
        if (existing.isPresent() && !forceRegenerate) return null;

        WeatherResDTO.WeatherClassificationResult classification = classifyWeatherForDate(region, date);
        if (!classification.isValid()) return null;

        WeatherTemplate template = WeatherRecommendationUtils.findMatchingTemplate(classification, templateMap);
        if (template == null) return null;

        existing.ifPresent(dailyRecommendationRepository::delete);

        dailyRecommendationRepository.save(
                DailyRecommendation.builder()
                        .region(region)
                        .weatherTemplate(template)
                        .forecastDate(date)
                        .build()
        );

        return new RecommendationResult(classification.weatherType(), existing.isEmpty());
    }

    private WeatherResDTO.WeatherClassificationResult classifyWeatherForDate(Region region, LocalDate date) {

        // 1. 두 데이터 소스 모두 조회
        List<RawMediumTermWeather> mediumTermData = mediumTermWeatherRepository.findLatestByRegionIdAndForecastDate(region.getId(), date);
        List<RawShortTermWeather> shortTermData = shortTermWeatherRepository.findLatestByRegionIdAndForecastDate(region.getId(), date);

        // 2. 스마트 우선순위 결정
        DataSourceDecision decision = determineOptimalDataSource(date, shortTermData, mediumTermData);

        // 3. 결정된 데이터 소스 사용
        return switch (decision.source()) {
            case SHORT_TERM -> {
                log.debug("단기예보 사용: {} (이유: {})", date, decision.reason());
                yield classificationService.classifyShortTermWeatherWithCentralTemp(shortTermData, region.getId(), date);
            }
            case MEDIUM_TERM -> {
                log.debug("중기예보 사용: {} (이유: {})", date, decision.reason());
                yield classificationService.classifyMediumTermWeather(mediumTermData, region.getId(), date);
            }
            case NONE -> {
                log.error("사용 가능한 날씨 데이터 없음: {}", date);
                throw new WeatherException(WeatherErrorCode.WEATHER_DATA_NOT_FOUND);
            }
        };
    }

    private DataSourceDecision determineOptimalDataSource(LocalDate targetDate,
                                                          List<RawShortTermWeather> shortTermData,
                                                          List<RawMediumTermWeather> mediumTermData) {

        LocalDate today = LocalDate.now();
        long daysFromToday = ChronoUnit.DAYS.between(today, targetDate);

        // 1. 데이터 존재 여부 확인
        boolean hasShortTerm = shortTermData != null && !shortTermData.isEmpty();
        boolean hasMediumTerm = mediumTermData != null && !mediumTermData.isEmpty();

        if (!hasShortTerm && !hasMediumTerm) {
            return new DataSourceDecision(DataSource.NONE, "데이터 없음");
        }

        // 2. 단기예보 우선 범위 (0~3일): 단기예보가 더 정확
        if (daysFromToday >= 0 && daysFromToday <= 3) {
            if (hasShortTerm) {
                // 단기예보 데이터 품질 검사
                if (shortTermData.size() >= 8) {
                    return new DataSourceDecision(DataSource.SHORT_TERM,
                            String.format("단기예보 범위 내(%d일 후), 충분한 데이터(%d개)", daysFromToday, shortTermData.size()));
                } else {
                    log.warn("단기예보 데이터 부족: {}일 후, {}개 데이터", daysFromToday, shortTermData.size());
                    return hasMediumTerm ?
                            new DataSourceDecision(DataSource.MEDIUM_TERM, "단기예보 데이터 부족으로 중기예보 사용") :
                            new DataSourceDecision(DataSource.SHORT_TERM, "단기예보 데이터 부족하지만 중기예보 없음");
                }
            } else {
                return new DataSourceDecision(DataSource.MEDIUM_TERM, "단기예보 없음");
            }
        }

        // 3. 중기예보 우선 범위 (4일~): 중기예보가 적절
        else if (daysFromToday >= 4) {
            if (hasMediumTerm) {
                return new DataSourceDecision(DataSource.MEDIUM_TERM,
                        String.format("중기예보 범위 내(%d일 후)", daysFromToday));
            } else {
                return new DataSourceDecision(DataSource.SHORT_TERM, "중기예보 없어서 단기예보 사용");
            }
        }

        // 4. 과거 날짜: 단기예보 우선 (더 정확했던 데이터)
        else {
            if (hasShortTerm) {
                return new DataSourceDecision(DataSource.SHORT_TERM,
                        String.format("과거 날짜(%d일 전), 단기예보 우선", Math.abs(daysFromToday)));
            } else {
                return new DataSourceDecision(DataSource.MEDIUM_TERM, "과거 날짜, 단기예보 없음");
            }
        }
    }

    private WeatherResDTO.DailyPrecipitation getPrecipitationForDateOptimized(
            LocalDate date,
            List<RawMediumTermWeather> mediumTermData,
            List<RawShortTermWeather> shortTermData) {
        try {
            // 1. 중기예보 우선 사용
            if (mediumTermData != null && !mediumTermData.isEmpty()) {
                RawMediumTermWeather data = WeatherClassificationUtils
                        .selectRepresentativeMediumTermData(mediumTermData, date);

                return WeatherResDTO.DailyPrecipitation.builder()
                        .forecastDate(date)
                        .precipitationProbability(data.getPrecipitationProbability())
                        .build();
            }

            // 2. 단기예보 사용
            if (shortTermData != null && !shortTermData.isEmpty()) {
                RawShortTermWeather data = WeatherClassificationUtils
                        .selectRepresentativeShortTermData(shortTermData, date);

                return WeatherResDTO.DailyPrecipitation.builder()
                        .forecastDate(date)
                        .precipitationProbability(data.getPrecipitationProbability())
                        .build();
            }

            // 3. 데이터가 없는 경우
            log.debug("강수확률 데이터 없음: date={}", date);
            return WeatherResDTO.DailyPrecipitation.builder()
                    .forecastDate(date)
                    .precipitationProbability(null)
                    .build();

        } catch (Exception e) {
            log.error("강수확률 조회 중 오류 발생: date={}", date, e);
            return WeatherResDTO.DailyPrecipitation.builder()
                    .forecastDate(date)
                    .precipitationProbability(null)
                    .build();
        }
    }

    private Region validateRegionExists(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> {
                    log.error("존재하지 않는 지역: regionId={}", regionId);
                    return new WeatherException(WeatherErrorCode.REGION_NOT_FOUND);
                });
    }

    private record RegionRecommendationResult(int recommendationsGenerated, int newRecommendations,
                                              int updatedRecommendations, List<String> processedDates,
                                              Map<WeatherType, Integer> weatherTypeStats) {}

    private record RecommendationResult(WeatherType weatherType, boolean isNew) {}

    /**
     * 데이터 소스 결정 결과
     */
    private record DataSourceDecision(DataSource source, String reason) {}

    /**
     * 데이터 소스 열거형
     */
    private enum DataSource {
        SHORT_TERM, MEDIUM_TERM, NONE
    }
}
