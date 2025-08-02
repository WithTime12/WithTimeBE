package org.withtime.be.withtimebe.domain.date.preference.dto;

import lombok.Builder;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferencePartType;
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

    @Builder
    public record TestResult(
            PreferenceType preferenceType,
            Double aPercentage,
            Double bPercentage,
            Double cPercentage,
            Double dPercentage,
            TypeDescription typeDescription,
            PartTypeDescriptions partTypeDescriptions
    ) {

    }

    @Builder
    public record TypeDescription(
            String symbolicAnimal,
            PreferenceType preferenceType,
            String simpleDescription,
            String analysis
    ) {

    }

    @Builder
    public record PartTypeDescriptions(
            List<PartTypeDescription> types,
            Integer size
    ) {

    }

    @Builder
    public record PartTypeDescription(
            PreferencePartType typeInitial,
            String typeEng,
            String type,
            String description
    ) {

    }

}
