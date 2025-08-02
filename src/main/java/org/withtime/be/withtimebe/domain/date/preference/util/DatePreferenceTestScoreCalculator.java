package org.withtime.be.withtimebe.domain.date.preference.util;

import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceRequestDTO;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceTestResult;

public interface DatePreferenceTestScoreCalculator {
    DatePreferenceTestResult calculateTestScore(DatePreferenceRequestDTO.Test request);
}
