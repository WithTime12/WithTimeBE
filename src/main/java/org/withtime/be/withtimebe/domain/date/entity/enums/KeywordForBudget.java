package org.withtime.be.withtimebe.domain.date.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.withtime.be.withtimebe.domain.weather.entity.Keyword;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum KeywordForBudget {
    RETRO_ALLEY("레트로 골목", BudgetLevel.FREE),
    BOOK_CAFE("북카페/책방", BudgetLevel.FREE),
    EXHIBITION("전시 공간", BudgetLevel.FREE),
    WALK("산책 중심", BudgetLevel.FREE),

    KOREAN("한식", BudgetLevel.LOW),
    BRUNCH_CAFE("브런치 카페", BudgetLevel.LOW),
    DESSERT_CAFE("디저트 카페", BudgetLevel.LOW),
    VIEW_SPOT("전망 좋은 곳", BudgetLevel.LOW),

    WESTERN("양식", BudgetLevel.MEDIUM),
    ROOFTOP_CAFE("루프탑 카페", BudgetLevel.MEDIUM),
    SENSUAL("감성적인", BudgetLevel.MEDIUM),
    HANDCRAFT("수공예 체험 공간", BudgetLevel.MEDIUM),

    FUSION("퓨전 음식점", BudgetLevel.HIGH),
    PUB("이자카야/펍", BudgetLevel.HIGH),
    HYPER_SENSUAL("감각적인", BudgetLevel.HIGH);

    private final String label;
    private final BudgetLevel budgetLevel;

    public static List<KeywordForBudget> getKeywordsByBudget(BudgetLevel budget) {
        return Arrays.stream(KeywordForBudget.values())
                .filter(k -> k.getBudgetLevel().ordinal() <= budget.ordinal())
                .toList();
    }

}
