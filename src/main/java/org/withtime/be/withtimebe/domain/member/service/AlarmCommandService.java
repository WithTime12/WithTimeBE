package org.withtime.be.withtimebe.domain.member.service;

import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface AlarmCommandService {
    void send(Member member, AlarmRequestDTO.SendAlarm... request);
}
