package org.withtime.be.withtimebe.domain.date.preference.repository;

import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceTestResult;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface DatePreferenceTestResultRepository {
    DatePreferenceTestResult save(DatePreferenceTestResult datePreferenceTestResult);
    void deleteAllByMember(Member member);
}
