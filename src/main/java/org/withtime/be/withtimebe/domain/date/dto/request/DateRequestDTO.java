package org.withtime.be.withtimebe.domain.date.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.withtime.be.withtimebe.domain.date.entity.enums.DatePriceRange;
import org.withtime.be.withtimebe.domain.date.entity.enums.DateTime;
import org.withtime.be.withtimebe.domain.date.entity.enums.MealType;
import org.withtime.be.withtimebe.domain.date.entity.enums.Transportation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record DateRequestDTO() {


    public record CreateDateCourse(
            @NotNull(message = "예산을 선택해주세요")
            DatePriceRange budget,

            @Size(min = 1, message = "최소 하나 이상의 값을 선택해주세요")
            @NotNull(message = "값이 비어있을 수 없습니다")
            List<String> datePlaces,

            @NotNull(message = "데이트 시간을 선택해주세요")
            DateTime dateDurationTime,

            List<MealType> mealPlan,

            @NotNull(message = "이동 수단을 선택해주세요")
            Transportation transportation,

            @Size(min = 1, max = 3)
            @NotNull(message = "사용자 취향을 선택해주세요")
            List<String> userPreferredKeywords,

            @NotNull(message = "startTime은 필수입니다")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
            LocalDateTime startTime,

            // 이미 보여줬던 코스의 시그니처(예: "12-45-33")
            Set<String> excludedCourseSignatures
    ) {}

    public record SaveDateCourse(
        List<Long> datePlaceIds,
        String name
    ){}

    public record DateCourseSearchCond(
            DatePriceRange budget,
            List<String> datePlaces,
            DateTime dateDurationTime,
            List<MealType> mealTypes,
            Transportation transportation,
            List<String> userPreferredKeywords
    ){}
}
