package org.withtime.be.withtimebe.domain.date.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
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
            @Schema(example = "UNDER_10K")
            DatePriceRange budget,

            @Size(min = 1, message = "최소 하나 이상의 값을 선택해주세요")
            @NotNull(message = "값이 비어있을 수 없습니다")
            @Schema(example = "[\"서울 종로구\"]")
            List<String> datePlaces,

            @NotNull(message = "데이트 시간을 선택해주세요")
            @Schema(example = "ONETOTWO")
            DateTime dateDurationTime,

            @Schema(example = "[\"BREAKFAST\"]")
            List<MealType> mealPlan,

            @NotNull(message = "이동 수단을 선택해주세요")
            @Schema(example = "WALK")
            Transportation transportation,

            @Size(min = 1, max = 3)
            @NotNull(message = "사용자 취향을 선택해주세요")
            @Schema(example = "[\"레트로 골목\", \"카페\"]")
            List<String> userPreferredKeywords,

            @NotBlank(message = "startTime은 필수입니다")
            @Pattern(
                    regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}$",
                    message = "startTime 형식은 yyyy-MM-dd'T'HH:mm 이어야 합니다"
            )
            @JsonProperty("startTime")
            @Schema(example = "2025-08-14T07:45")
            LocalDateTime startTime,

            // 이미 보여줬던 코스의 시그니처(예: "12-45-33")
            @Schema(example = "[]")
            Set<String> excludedCourseSignatures
    ) {}

    public record SaveDateCourse(
        List<Long> datePlaceIds,
        String name,
        DateCourseSearchCond dateCourseSearchCond
    ){}

    @Builder
    public record DateCourseSearchCond(
            DatePriceRange datePriceRange,
            List<String> datePlaces,
            DateTime dateDurationTime,
            @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true)
            List<MealType> mealTypes,
            Transportation transportation,
            @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true)
            List<String> userPreferredKeywords
    ){}
}
