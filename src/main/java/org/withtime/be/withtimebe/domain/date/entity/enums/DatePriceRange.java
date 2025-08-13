package org.withtime.be.withtimebe.domain.date.entity.enums;

import lombok.AllArgsConstructor;
import org.withtime.be.withtimebe.global.error.code.DateCourseErrorCode;
import org.withtime.be.withtimebe.global.error.exception.DateCourseException;

@AllArgsConstructor
public enum DatePriceRange {
    UNDER_10K("1만원 이하", 0, 10000),
    FROM_10K_TO_20K("1~2만원", 10000, 20000),
    FROM_20K_TO_30K("2~3만원", 20000, 30000),
    OVER_30K("3만원", 30000, Integer.MAX_VALUE),
    ;

    private final String label;
    private final int minPrice;
    private final int maxPrice;

    public static DatePriceRange fromPrice(int price){
        for (DatePriceRange range : values()){
            if (price >= range.minPrice && price < range.maxPrice){
                return range;
            }
        }
        throw new DateCourseException(DateCourseErrorCode.DateCourse_INVALID_BUDGET_RANGE);
    }
}
