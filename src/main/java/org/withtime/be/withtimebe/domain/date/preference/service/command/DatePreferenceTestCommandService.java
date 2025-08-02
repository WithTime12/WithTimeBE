package org.withtime.be.withtimebe.domain.date.preference.service.command;

import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceRequestDTO;
import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface DatePreferenceTestCommandService {
    DatePreferenceResponseDTO.TestResult test(Member member, DatePreferenceRequestDTO.Test request);
}
