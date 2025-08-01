package org.withtime.be.withtimebe.domain.date.preference.dto;

import lombok.Builder;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceType;

import java.util.List;

public record DatePreferenceResponseDTO() {

    @Builder
    public record FindTypes(
            List<FindType> types,
            Integer size
    ) {

    }

    @Builder
    public record FindType(
            String symbolicAnimal,
            PreferenceType preferenceType,
            String simpleDescription
    ) {

    }

    @Builder
    public record FindQuestions(
            List<FindQuestion> questions,
            Integer size
    ) {

    }

    @Builder
    public record FindQuestion(
            String question,
            String firstAnswer,
            String secondAnswer
    ) {

    }


}
