package org.withtime.be.withtimebe.domain.date.preference.service.query;

import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceQuestion;

import java.util.List;

public interface DatePreferenceQuestionQueryService {
    List<DatePreferenceQuestion> findQuestions();
}
