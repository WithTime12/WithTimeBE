package org.withtime.be.withtimebe.domain.date.preference.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceQuestion;
import org.withtime.be.withtimebe.domain.date.preference.repository.DatePreferenceQuestionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatePreferenceQuestionQueryServiceImpl implements DatePreferenceQuestionQueryService {

    private final DatePreferenceQuestionRepository datePreferenceQuestionRepository;

    @Override
    public List<DatePreferenceQuestion> findQuestions() {
        return datePreferenceQuestionRepository.findAllByOrderByIdAsc();
    }
}
