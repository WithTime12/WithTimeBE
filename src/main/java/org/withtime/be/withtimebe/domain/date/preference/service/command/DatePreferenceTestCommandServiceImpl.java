package org.withtime.be.withtimebe.domain.date.preference.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.date.preference.converter.DatePreferenceConverter;
import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceRequestDTO;
import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceResponseDTO;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferencePartDescription;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceTestResult;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferencePartType;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceType;
import org.withtime.be.withtimebe.domain.date.preference.repository.DatePreferenceDescriptionRepository;
import org.withtime.be.withtimebe.domain.date.preference.repository.DatePreferencePartDescriptionRepository;
import org.withtime.be.withtimebe.domain.date.preference.repository.DatePreferenceQuestionRepository;
import org.withtime.be.withtimebe.domain.date.preference.repository.DatePreferenceTestResultRepository;
import org.withtime.be.withtimebe.domain.date.preference.util.DatePreferenceTestScoreCalculator;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.global.error.code.DatePreferenceErrorCode;
import org.withtime.be.withtimebe.global.error.exception.DatePreferenceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DatePreferenceTestCommandServiceImpl implements DatePreferenceTestCommandService {

    private final DatePreferenceDescriptionRepository datePreferenceDescriptionRepository;
    private final DatePreferencePartDescriptionRepository datePreferencePartDescriptionRepository;
    private final DatePreferenceTestResultRepository datePreferenceTestResultRepository;
    private final DatePreferenceQuestionRepository datePreferenceQuestionRepository;
    private final DatePreferenceTestScoreCalculator datePreferenceTestScoreCalculator;

    @Override
    public DatePreferenceResponseDTO.TestResult test(Member member, DatePreferenceRequestDTO.Test request) {
        // valid 판단
        if (!validateRequest(request)) {
            throw new DatePreferenceException(DatePreferenceErrorCode.INVALID_ANSWERS);
        }

        // 점수 계산
        DatePreferenceTestResult result = datePreferenceTestScoreCalculator.calculateTestScore(request);
        result.mappingMember(member);

        // 엔티티 저장 (로그)
        datePreferenceTestResultRepository.save(result);

        // 응답 생성
        return buildResponse(result);
    }

    private boolean validateRequest(DatePreferenceRequestDTO.Test request) {
        return datePreferenceQuestionRepository.count() == request.answers().size() && request.answers().stream().allMatch(value -> value >= 1 && value <= 2);
    }

    private DatePreferenceResponseDTO.TestResult buildResponse(DatePreferenceTestResult testResult) {
        List<DatePreferencePartDescription> partDescriptionList = new ArrayList<>();
        PreferenceType preferenceType = testResult.getPreferenceType();
        for (int i = 0; i < 4; i++) {
            PreferencePartType preferencePartType = PreferencePartType.valueOf(preferenceType.name().substring(i, i + 1));
            datePreferencePartDescriptionRepository.findByPreferencePartType(preferencePartType).ifPresent(partDescriptionList::add);
        }
        return DatePreferenceConverter.toTestResult(
                testResult,
                datePreferenceDescriptionRepository.findByPreferenceType(preferenceType).orElse(null),
                partDescriptionList
        );
    }
}
