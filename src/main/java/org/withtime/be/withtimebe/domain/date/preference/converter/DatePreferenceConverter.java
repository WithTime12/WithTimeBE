package org.withtime.be.withtimebe.domain.date.preference.converter;

import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceResponseDTO;
import org.withtime.be.withtimebe.domain.date.preference.entity.*;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceType;

import java.util.List;

public class DatePreferenceConverter {
    public static DatePreferenceResponseDTO.FindTypes toFindTypes(List<DatePreferenceDescription> datePreferenceDescriptions) {
        return DatePreferenceResponseDTO.FindTypes.builder()
                .types(datePreferenceDescriptions.stream().map(DatePreferenceConverter::toFindType).toList())
                .size(datePreferenceDescriptions.size())
                .build();
    }

    public static DatePreferenceResponseDTO.FindType toFindType(DatePreferenceDescription datePreferenceDescription) {
        return DatePreferenceResponseDTO.FindType.builder()
                .symbolicAnimal(datePreferenceDescription.getSymbolicAnimal())
                .preferenceType(datePreferenceDescription.getPreferenceType())
                .simpleDescription(datePreferenceDescription.getSimpleDescription())
                .build();
    }

    public static DatePreferenceResponseDTO.FindQuestions toFindQuestions(List<DatePreferenceQuestion> datePreferenceQuestions) {
        return DatePreferenceResponseDTO.FindQuestions.builder()
                .questions(datePreferenceQuestions.stream().map(DatePreferenceConverter::toFindQuestion).toList())
                .size(datePreferenceQuestions.size())
                .build();
    }

    public static DatePreferenceResponseDTO.FindQuestion toFindQuestion(DatePreferenceQuestion datePreferenceQuestion) {
        return DatePreferenceResponseDTO.FindQuestion.builder()
                .question(datePreferenceQuestion.getQuestion())
                .firstAnswer(datePreferenceQuestion.getFirstAnswer())
                .secondAnswer(datePreferenceQuestion.getSecondAnswer())
                .build();
    }

    public static DatePreferenceResponseDTO.TestResult toTestResult(DatePreferenceTestResult datePreferenceTestResult,
                                                                    DatePreferenceDescription datePreferenceDescription,
                                                                    List<DatePreferencePartDescription> datePreferencePartDescriptions) {
        return DatePreferenceResponseDTO.TestResult.builder()
                .preferenceType(datePreferenceTestResult.getPreferenceType())
                .aPercentage(datePreferenceTestResult.getAPercentage())
                .bPercentage(datePreferenceTestResult.getBPercentage())
                .cPercentage(datePreferenceTestResult.getCPercentage())
                .dPercentage(datePreferenceTestResult.getDPercentage())
                .typeDescription(datePreferenceDescription == null ? null : DatePreferenceConverter.toTypeDescription(datePreferenceDescription))
                .partTypeDescriptions(datePreferencePartDescriptions == null || datePreferencePartDescriptions.isEmpty() ? null : DatePreferenceConverter.toPartTypeDescriptions(datePreferencePartDescriptions))
                .build();
    }

    public static DatePreferenceTestResult toDatePreferenceTestResult(PreferenceType preferenceType,
                                                                       Double aPercentage,
                                                                       Double bPercentage,
                                                                       Double cPercentage,
                                                                       Double dPercentage) {
        return DatePreferenceTestResult.builder()
                .preferenceType(preferenceType)
                .aPercentage(aPercentage)
                .bPercentage(bPercentage)
                .cPercentage(cPercentage)
                .dPercentage(dPercentage)
                .build();
    }

    public static DatePreferenceResponseDTO.TypeDescription toTypeDescription(DatePreferenceDescription datePreferenceDescription) {
        return DatePreferenceResponseDTO.TypeDescription.builder()
                .symbolicAnimal(datePreferenceDescription.getSymbolicAnimal())
                .preferenceType(datePreferenceDescription.getPreferenceType())
                .simpleDescription(datePreferenceDescription.getSimpleDescription())
                .analysis(datePreferenceDescription.getAnalysis())
                .build();
    }

    public static DatePreferenceResponseDTO.PartTypeDescriptions toPartTypeDescriptions(List<DatePreferencePartDescription> descriptions) {
        return DatePreferenceResponseDTO.PartTypeDescriptions.builder()
                .types(descriptions.stream().map(DatePreferenceConverter::toPartTypeDescription).toList())
                .size(descriptions.size())
                .build();
    }

    public static DatePreferenceResponseDTO.PartTypeDescription toPartTypeDescription(DatePreferencePartDescription description) {
        return DatePreferenceResponseDTO.PartTypeDescription.builder()
                .typeInitial(description.getPreferencePartType())
                .type(description.getType())
                .typeEng(description.getTypeEng())
                .description(description.getDescription())
                .build();
    }

    public static DatePreferenceResponseDTO.FindRelationType toFindRelationType(DatePreferenceTypeRelation best, DatePreferenceTypeRelation worst, DatePreferenceDescription bestDescription, DatePreferenceDescription worstDescription) {
        return DatePreferenceResponseDTO.FindRelationType.builder()
                .bestType(best.getType())
                .bestReason(best.getReason())
                .worstType(worst.getType())
                .worstReason(worst.getReason())
                .bestTypeDescription(toTypeDescription(bestDescription))
                .worstTypeDescription(toTypeDescription(worstDescription))
                .build();
    }
}
