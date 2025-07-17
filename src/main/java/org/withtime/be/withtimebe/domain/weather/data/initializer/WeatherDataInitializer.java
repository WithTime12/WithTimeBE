package org.withtime.be.withtimebe.domain.weather.data.initializer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.weather.entity.Keyword;
import org.withtime.be.withtimebe.domain.weather.entity.TemplateKeyword;
import org.withtime.be.withtimebe.domain.weather.entity.WeatherTemplate;
import org.withtime.be.withtimebe.domain.weather.entity.enums.PrecipCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.TempCategory;
import org.withtime.be.withtimebe.domain.weather.entity.enums.WeatherType;
import org.withtime.be.withtimebe.domain.weather.repository.KeywordRepository;
import org.withtime.be.withtimebe.domain.weather.repository.TemplateKeywordRepository;
import org.withtime.be.withtimebe.domain.weather.repository.WeatherTemplateRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherDataInitializer {

    private final KeywordRepository keywordRepository;
    private final WeatherTemplateRepository weatherTemplateRepository;
    private final TemplateKeywordRepository templateKeywordRepository;

    @PostConstruct
    @Transactional
    public void init() {
        initKeywords();
        initWeatherTemplates();
        initTemplateKeywords();
    }

    private void initKeywords() {
        List<String> keywordNames = List.of(
                "활발한 활동", "전망 좋은 곳", "활기찬",
                "감성적인", "탐험 중심", "쇼핑+데이트 복합",
                "느긋하게 쉬기", "사진중심", "전시 공간", "북카페/책방"
        );

        List<String> existing = keywordRepository.findAll().stream()
                .map(Keyword::getName)
                .toList();

        List<Keyword> newKeywords = keywordNames.stream()
                .filter(name -> !existing.contains(name))
                .map(name -> Keyword.builder().name(name).build())
                .toList();

        keywordRepository.saveAll(newKeywords);
        log.info("[ WeatherDataInitializer ] 키워드 데이터 초기화 완료 (새로 저장된 키워드 수: {})", newKeywords.size());
    }

    private void initWeatherTemplates() {
        if (weatherTemplateRepository.count() > 0) {
            log.info("[ WeatherDataInitializer ] 템플릿이 이미 존재합니다. 초기화 생략");
            return;
        }

        List<WeatherTemplate> templates = List.of(
                WeatherTemplate.builder().weatherType(WeatherType.CLEAR).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.VERY_LOW).message("맑은 하늘과 쌀쌀한 날씨가 예정되어 있습니다...").emoji("sunny").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLEAR).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.VERY_LOW).message("맑은 하늘과 선선한 날씨가 예정되어 있습니다...").emoji("sunny").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLEAR).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.VERY_LOW).message("맑은 하늘과 무난한 날씨가 예정되어 있습니다...").emoji("sunny").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLEAR).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.VERY_LOW).message("맑은 하늘과 무더운 날씨가 예정되어 있습니다...").emoji("sunny").build(),

                WeatherTemplate.builder().weatherType(WeatherType.CLOUDY).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.LOW).message("흐린 하늘과 쌀쌀한 날씨가 예정되어 있습니다...").emoji("cloudy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLOUDY).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.LOW).message("흐린 하늘과 선선한 날씨가 예정되어 있습니다...").emoji("cloudy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLOUDY).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.LOW).message("흐린 하늘과 무난한 날씨가 예정되어 있습니다...").emoji("cloudy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLOUDY).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.LOW).message("흐린 하늘과 무더운 날씨가 예정되어 있습니다...").emoji("cloudy").build(),

                WeatherTemplate.builder().weatherType(WeatherType.RAINY).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.HIGH).message("비와 함께 쌀쌀한 날씨가 예정되어 있습니다...").emoji("rainy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.RAINY).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.HIGH).message("비와 함께 선선한 날씨가 예정되어 있습니다...").emoji("rainy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.RAINY).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.HIGH).message("비와 함께 무난한 날씨가 예정되어 있습니다...").emoji("rainy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.RAINY).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.HIGH).message("비와 함께 무더운 날씨가 예정되어 있습니다...").emoji("rainy").build(),

                WeatherTemplate.builder().weatherType(WeatherType.SNOWY).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.LOW).message("눈과 함께 쌀쌀한 날씨가 예정되어 있습니다...").emoji("snowy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SNOWY).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.LOW).message("눈과 함께 선선한 날씨가 예정되어 있습니다...").emoji("snowy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SNOWY).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.LOW).message("눈과 함께 무난한 날씨가 예정되어 있습니다...").emoji("snowy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SNOWY).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.LOW).message("눈과 함께 무더운 날씨가 예정되어 있습니다...").emoji("snowy").build(),

                WeatherTemplate.builder().weatherType(WeatherType.RAIN_SNOW).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.HIGH).message("비/눈이 같이오는 쌀쌀한 날씨가 예정되어 있습니다...").emoji("rainy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.RAIN_SNOW).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.HIGH).message("비/눈이 같이오는 선선한 날씨가 예정되어 있습니다...").emoji("rainy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.RAIN_SNOW).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.HIGH).message("비/눈이 같이오는 무난한 날씨가 예정되어 있습니다...").emoji("rainy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.RAIN_SNOW).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.HIGH).message("비/눈이 같이오는 무더운 날씨가 예정되어 있습니다...").emoji("rainy").build(),

                WeatherTemplate.builder().weatherType(WeatherType.SHOWER).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.LOW).message("소나기가 예정된 쌀쌀한 날씨가 예정되어 있습니다...").emoji("shower").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SHOWER).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.LOW).message("소나기가 예정된 선선한 날씨가 예정되어 있습니다...").emoji("shower").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SHOWER).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.LOW).message("소나기가 예정된 무난한 날씨가 예정되어 있습니다...").emoji("shower").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SHOWER).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.LOW).message("소나기가 예정된 무더운 날씨가 예정되어 있습니다...").emoji("shower").build()
        );

        weatherTemplateRepository.saveAll(templates);
        log.info("[ WeatherDataInitializer ] 템플릿 {}개 저장 완료", templates.size());
    }

    private void initTemplateKeywords() {
        List<WeatherTemplate> templates = weatherTemplateRepository.findAll();
        List<Keyword> keywords = keywordRepository.findAll();
        List<TemplateKeyword> newMappings = new ArrayList<>();

        Set<String> existingMappings = templateKeywordRepository.findAll().stream()
                .map(tk -> tk.getWeatherTemplate().getId() + "-" + tk.getKeyword().getId())
                .collect(Collectors.toSet());

        for (WeatherTemplate template : templates) {
            for (Keyword keyword : keywords) {
                if (!shouldMap(template.getWeatherType(), keyword.getName())) continue;

                String key = template.getId() + "-" + keyword.getId();

                if (existingMappings.contains(key)) continue;

                newMappings.add(TemplateKeyword.builder()
                        .weatherTemplate(template)
                        .keyword(keyword)
                        .build());
            }
        }

        templateKeywordRepository.saveAll(newMappings);
        log.info("[ WeatherDataInitializer ] 템플릿-키워드 매핑 완료 (새로 추가된 매핑 수: {})", newMappings.size());
    }

    private boolean shouldMap(WeatherType weather, String keyword) {
        return switch (weather) {
            case CLEAR -> List.of("활발한 활동", "전망 좋은 곳", "활기찬").contains(keyword);
            case CLOUDY -> List.of("감성적인", "탐험 중심", "쇼핑+데이트 복합").contains(keyword);
            case RAINY -> List.of("감성적인", "쇼핑+데이트 복합", "느긋하게 쉬기").contains(keyword);
            case SNOWY -> List.of("감성적인", "전망 좋은 곳", "사진중심").contains(keyword);
            case RAIN_SNOW -> List.of("감성적인", "사진중심", "전시 공간").contains(keyword);
            case SHOWER -> List.of("사진중심", "북카페/책방", "느긋하게 쉬기").contains(keyword);
        };
    }
}
