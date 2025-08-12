package org.withtime.be.withtimebe.domain.date.preference.service.command;

import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface DatePreferenceTestResultCommandService {
    void resetDatePreferenceData(Member member);
}
