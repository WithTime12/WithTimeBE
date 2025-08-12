package org.withtime.be.withtimebe.domain.date.preference.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.date.preference.repository.DatePreferenceTestResultRepository;
import org.withtime.be.withtimebe.domain.member.entity.Member;

@Service
@Transactional
@RequiredArgsConstructor
public class DatePreferenceTestResultCommandImpl implements DatePreferenceTestResultCommandService {

    private final DatePreferenceTestResultRepository datePreferenceTestResultRepository;

    @Override
    public void resetDatePreferenceData(Member member) {
        datePreferenceTestResultRepository.deleteAllByMember(member);
    }
}
