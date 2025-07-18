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
                // ============ 1. 맑음 (CLEAR) - 강수확률 낮음만 ============
                // 맑음 + 모든 기온 + 강수확률 없음/매우 낮음
                WeatherTemplate.builder().weatherType(WeatherType.CLEAR).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.NONE).message("맑은 하늘과 쌀쌀한 날씨가 예정되어 있습니다.날씨가 맑은 오늘, 야외에서 기분 좋은 데이트를 즐겨보세요!").emoji("sunny️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLEAR).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.VERY_LOW).message("맑은 하늘과 쌀쌀한 날씨가 예정되어 있습니다.날씨가 맑은 오늘, 야외에서 기분 좋은 데이트를 즐겨보세요!").emoji("sunny️").build(),

                WeatherTemplate.builder().weatherType(WeatherType.CLEAR).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.NONE).message("맑은 하늘과 선선한 날씨가 예정되어 있습니다.날씨가 맑은 오늘, 야외에서 기분 좋은 데이트를 즐겨보세요!").emoji("sunny️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLEAR).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.VERY_LOW).message("맑은 하늘과 선선한 날씨가 예정되어 있습니다.날씨가 맑은 오늘, 야외에서 기분 좋은 데이트를 즐겨보세요!").emoji("sunny️").build(),

                WeatherTemplate.builder().weatherType(WeatherType.CLEAR).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.NONE).message("맑은 하늘과 무난한 날씨가 예정되어 있습니다.날씨가 맑은 오늘, 야외에서 기분 좋은 데이트를 즐겨보세요!").emoji("sunny️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLEAR).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.VERY_LOW).message("맑은 하늘과 무난한 날씨가 예정되어 있습니다.날씨가 맑은 오늘, 야외에서 기분 좋은 데이트를 즐겨보세요!").emoji("sunny️").build(),

                WeatherTemplate.builder().weatherType(WeatherType.CLEAR).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.NONE).message("맑은 하늘과 무더운 날씨가 예정되어 있습니다.날씨가 맑은 오늘, 야외에서 기분 좋은 데이트를 즐겨보세요!").emoji("sunny️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLEAR).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.VERY_LOW).message("맑은 하늘과 무더운 날씨가 예정되어 있습니다.날씨가 맑은 오늘, 야외에서 기분 좋은 데이트를 즐겨보세요!").emoji("sunny️").build(),

                // ============ 2. 흐림 (CLOUDY) - 중간 강수확률 ============
                // 흐림 + 모든 기온 + 약간 낮음/낮음/높음
                WeatherTemplate.builder().weatherType(WeatherType.CLOUDY).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.VERY_LOW).message("흐린 하늘과 쌀쌀한 날씨가 예정되어 있습니다.흐린 날씨엔 감성을 더한 골목 탐방이나 복합공간 데이트가 잘 어울려요.").emoji("cloudy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLOUDY).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.LOW).message("흐린 하늘과 쌀쌀한 날씨가 예정되어 있습니다.흐린 날씨엔 감성을 더한 골목 탐방이나 복합공간 데이트가 잘 어울려요.").emoji("cloudy").build(),

                WeatherTemplate.builder().weatherType(WeatherType.CLOUDY).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.VERY_LOW).message("흐린 하늘과 선선한 날씨가 예정되어 있습니다.흐린 날씨엔 감성을 더한 골목 탐방이나 복합공간 데이트가 잘 어울려요.").emoji("cloudy️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLOUDY).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.LOW).message("흐린 하늘과 선선한 날씨가 예정되어 있습니다.흐린 날씨엔 감성을 더한 골목 탐방이나 복합공간 데이트가 잘 어울려요.").emoji("cloudy").build(),

                WeatherTemplate.builder().weatherType(WeatherType.CLOUDY).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.VERY_LOW).message("흐린 하늘과 무난한 날씨가 예정되어 있습니다.흐린 날씨엔 감성을 더한 골목 탐방이나 복합공간 데이트가 잘 어울려요.").emoji("cloudy️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLOUDY).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.LOW).message("흐린 하늘과 무난한 날씨가 예정되어 있습니다.흐린 날씨엔 감성을 더한 골목 탐방이나 복합공간 데이트가 잘 어울려요.").emoji("cloudy").build(),

                WeatherTemplate.builder().weatherType(WeatherType.CLOUDY).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.VERY_LOW).message("흐린 하늘과 무더운 날씨가 예정되어 있습니다.흐린 날씨엔 감성을 더한 골목 탐방이나 복합공간 데이트가 잘 어울려요.").emoji("cloudy️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.CLOUDY).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.LOW).message("흐린 하늘과 무더운 날씨가 예정되어 있습니다.흐린 날씨엔 감성을 더한 골목 탐방이나 복합공간 데이트가 잘 어울려요.").emoji("cloudy").build(),

                // ============ 3. 비 (RAINY) - 강수확률 높음만 ============
                // 비 + 모든 기온 + 높음/매우 높음
                WeatherTemplate.builder().weatherType(WeatherType.RAINY).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.HIGH).message("비와 함께 쌀쌀한 날씨가 예정되어 있습니다.비 오는 날엔 실내에서 여유롭게 보내는 감성 데이트를 추천해요.").emoji("rainy").build(),
                WeatherTemplate.builder().weatherType(WeatherType.RAINY).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.VERY_HIGH).message("비와 함께 쌀쌀한 날씨가 예정되어 있습니다.우산이 필요한 오늘, 조용한 실내 공간에서 감성 가득한 시간을 보내보세요.").emoji("rainy️").build(),

                WeatherTemplate.builder().weatherType(WeatherType.RAINY).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.HIGH).message("비와 함께 선선한 날씨가 예정되어 있습니다.비 오는 날엔 실내에서 여유롭게 보내는 감성 데이트를 추천해요.").emoji("rainy️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.RAINY).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.VERY_HIGH).message("비와 함께 선선한 날씨가 예정되어 있습니다.우산이 필요한 오늘, 조용한 실내 공간에서 감성 가득한 시간을 보내보세요.").emoji("rainy️").build(),

                WeatherTemplate.builder().weatherType(WeatherType.RAINY).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.HIGH).message("비와 함께 무난한 날씨가 예정되어 있습니다.비 오는 날엔 실내에서 여유롭게 보내는 감성 데이트를 추천해요.").emoji("rainy️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.RAINY).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.VERY_HIGH).message("비와 함께 무난한 날씨가 예정되어 있습니다.우산이 필요한 오늘, 조용한 실내 공간에서 감성 가득한 시간을 보내보세요.").emoji("rainy️").build(),

                WeatherTemplate.builder().weatherType(WeatherType.RAINY).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.HIGH).message("비와 함께 무더운 날씨가 예정되어 있습니다.비 오는 날엔 실내에서 여유롭게 보내는 감성 데이트를 추천해요.").emoji("rainy️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.RAINY).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.VERY_HIGH).message("비와 함께 무더운 날씨가 예정되어 있습니다.우산이 필요한 오늘, 조용한 실내 공간에서 감성 가득한 시간을 보내보세요.").emoji("rainy️").build(),

                // ============ 4. 눈 (SNOWY) - 추운 날씨만 ============
                // 눈 + 쌀쌀함/선선함만 + 약간 낮음/낮음/높음
                WeatherTemplate.builder().weatherType(WeatherType.SNOWY).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.VERY_LOW).message("눈과 함께 쌀쌀한 날씨가 예정되어 있습니다.하얀 눈과 함께 감성적인 장소에서 특별한 하루를 만들어보세요.").emoji("snowy️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SNOWY).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.LOW).message("눈과 함께 쌀쌀한 날씨가 예정되어 있습니다.하얀 눈과 함께 감성적인 장소에서 특별한 하루를 만들어보세요.").emoji("snowy️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SNOWY).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.HIGH).message("눈과 함께 쌀쌀한 날씨가 예정되어 있습니다.눈 내리는 풍경 속에서 감성과 추억이 가득한 데이트를 즐겨보세요.").emoji("snowy️").build(),

                WeatherTemplate.builder().weatherType(WeatherType.SNOWY).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.VERY_LOW).message("눈과 함께 선선한 날씨가 예정되어 있습니다.하얀 눈과 함께 감성적인 장소에서 특별한 하루를 만들어보세요.").emoji("snowy️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SNOWY).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.LOW).message("눈과 함께 선선한 날씨가 예정되어 있습니다.하얀 눈과 함께 감성적인 장소에서 특별한 하루를 만들어보세요.").emoji("snowy️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SNOWY).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.HIGH).message("눈과 함께 선선한 날씨가 예정되어 있습니다.눈 내리는 풍경 속에서 감성과 추억이 가득한 데이트를 즐겨보세요.").emoji("snowy️").build(),

                // ============ 5. 비/눈 (RAIN_SNOW) - 추운 날씨만 ============
                // 비/눈 + 쌀쌀함/선선함만 + 낮음/높음
                WeatherTemplate.builder().weatherType(WeatherType.RAIN_SNOW).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.LOW).message("비/눈이 같이오는 쌀쌀한 날씨가 예정되어 있습니다.변덕스러운 날씨엔 실내 전시나 감성 공간에서 편안한 데이트를 즐겨보세요.").emoji("rainy️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.RAIN_SNOW).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.HIGH).message("비/눈이 같이오는 쌀쌀한 날씨가 예정되어 있습니다.비와 눈이 섞인 날엔 실내에서 감성을 채우는 데이트가 좋아요.").emoji("rainy️").build(),

                WeatherTemplate.builder().weatherType(WeatherType.RAIN_SNOW).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.LOW).message("비/눈이 같이오는 선선한 날씨가 예정되어 있습니다.변덕스러운 날씨엔 실내 전시나 감성 공간에서 편안한 데이트를 즐겨보세요.").emoji("rainy️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.RAIN_SNOW).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.HIGH).message("비/눈이 같이오는 선선한 날씨가 예정되어 있습니다.비와 눈이 섞인 날엔 실내에서 감성을 채우는 데이트가 좋아요.").emoji("rainy️").build(),

                // ============ 6. 소나기 (SHOWER) - 모든 기온 가능 ============
                // 소나기 + 모든 기온 + 낮음/높음
                WeatherTemplate.builder().weatherType(WeatherType.SHOWER).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.LOW).message("소나기가 예정된 쌀쌀한 날씨가 예정되어 있습니다.소나기 예보가 있다면, 실내에서 여유롭게 보내는 하루는 어떠세요?").emoji("shower").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SHOWER).tempCategory(TempCategory.CHILLY).precipCategory(PrecipCategory.HIGH).message("소나기가 예정된 쌀쌀한 날씨가 예정되어 있습니다.갑작스러운 소나기를 피해, 조용한 북카페나 책방에서의 데이트를 추천해요.").emoji("shower️").build(),

                WeatherTemplate.builder().weatherType(WeatherType.SHOWER).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.LOW).message("소나기가 예정된 선선한 날씨가 예정되어 있습니다.소나기 예보가 있다면, 실내에서 여유롭게 보내는 하루는 어떠세요?").emoji("shower").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SHOWER).tempCategory(TempCategory.COOL).precipCategory(PrecipCategory.HIGH).message("소나기가 예정된 선선한 날씨가 예정되어 있습니다.갑작스러운 소나기를 피해, 조용한 북카페나 책방에서의 데이트를 추천해요.").emoji("shower️").build(),

                WeatherTemplate.builder().weatherType(WeatherType.SHOWER).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.LOW).message("소나기가 예정된 무난한 날씨가 예정되어 있습니다.소나기 예보가 있다면, 실내에서 여유롭게 보내는 하루는 어떠세요?").emoji("shower️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SHOWER).tempCategory(TempCategory.MILD).precipCategory(PrecipCategory.HIGH).message("소나기가 예정된 무난한 날씨가 예정되어 있습니다.갑작스러운 소나기를 피해, 조용한 북카페나 책방에서의 데이트를 추천해요.").emoji("shower️").build(),

                WeatherTemplate.builder().weatherType(WeatherType.SHOWER).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.LOW).message("소나기가 예정된 무더운 날씨가 예정되어 있습니다.소나기 예보가 있다면, 실내에서 여유롭게 보내는 하루는 어떠세요?").emoji("shower️").build(),
                WeatherTemplate.builder().weatherType(WeatherType.SHOWER).tempCategory(TempCategory.HOT).precipCategory(PrecipCategory.HIGH).message("소나기가 예정된 무더운 날씨가 예정되어 있습니다.갑작스러운 소나기를 피해, 조용한 북카페나 책방에서의 데이트를 추천해요.").emoji("shower️").build()
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
