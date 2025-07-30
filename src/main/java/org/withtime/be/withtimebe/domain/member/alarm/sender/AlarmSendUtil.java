package org.withtime.be.withtimebe.domain.member.alarm.sender;

import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface AlarmSendUtil<T> {
    void send(Member member, T message) throws Exception;
}
