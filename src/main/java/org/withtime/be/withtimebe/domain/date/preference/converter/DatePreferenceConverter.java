package org.withtime.be.withtimebe.domain.date.preference.converter;

import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceResponseDTO;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceDescription;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceQuestion;

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
}
