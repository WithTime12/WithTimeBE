package org.withtime.be.withtimebe.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.namul.api.payload.error.exception.ServerApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.member.alarm.factory.AlarmSenderFactory;
import org.withtime.be.withtimebe.domain.member.converter.AlarmConverter;
import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.dto.AlarmResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Alarm;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.repository.AlarmRepository;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;
import org.withtime.be.withtimebe.global.error.code.AlarmErrorCode;
import org.withtime.be.withtimebe.global.error.exception.AlarmException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmCommandServiceImpl implements AlarmCommandService {

    private final AlarmSenderFactory alarmSenderFactory;
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    @Override
    public void send(Member member, AlarmRequestDTO.SendAlarm... request) {
        // 알림 만들기
        List<Alarm> alarms = new ArrayList<>();
        for (AlarmRequestDTO.SendAlarm req : request) {
            try {
                sendAlarm(member, getScope(member), req);

                alarms.add(AlarmConverter.toAlarm(member, req));
            } catch (Exception ignored){}
        }
        alarmRepository.saveAll(alarms);
    }

    @Override
    public void updateDeviceToken(Member member, AlarmRequestDTO.UpdateDeviceToken request) {
        member.updateDeviceToken(request.deviceToken());
        memberRepository.save(member);
    }

    @Override
    public AlarmResponseDTO.UpdateSetting updateAlarmSetting(Member member, AlarmRequestDTO.UpdateSetting request) {
        member.updateAlarmSetting(
                request.pushAlarm(),
                request.emailAlarm(),
                request.smsAlarm()
        );
        memberRepository.save(member);
        return AlarmConverter.toUpdateSetting(member);
    }

    private List<Class<?>> getScope(Member member) {
        List<Class<?>> classes = new ArrayList<>();
        if (Boolean.TRUE.equals(member.getEmailAlarm())) {
            classes.add(alarmSenderFactory.getEmailAlarmClass());
        }
        if (Boolean.TRUE.equals(member.getPushAlarm())) {
            classes.add(alarmSenderFactory.getPushAlarmClass());
        }
        if (Boolean.TRUE.equals(member.getSmsAlarm())) {
            classes.add(alarmSenderFactory.getSMSAlarmClass());
        }
        return classes;
    }

    private void sendAlarm(Member member, List<Class<?>> alarmScope, AlarmRequestDTO.SendAlarm request) throws ServerApplicationException {
        alarmScope.forEach(clz -> {
            try {
                alarmSenderFactory.getAlarmSender(clz).send(member, request);
            } catch (NullPointerException e) {
                throw new AlarmException(AlarmErrorCode.NOT_FOUND_ALARM_SENDER);
            } catch (Exception e) {
                throw new AlarmException(AlarmErrorCode.ALARM_SEND_ERROR);
            }
        });
    }
}
