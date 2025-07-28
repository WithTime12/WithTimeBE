package org.withtime.be.withtimebe.domain.member.service;

import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.member.alarm.service.AlarmSender;
import org.withtime.be.withtimebe.domain.member.converter.AlarmConverter;
import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Alarm;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.repository.AlarmRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmCommandServiceImpl implements AlarmCommandService {

    private final AlarmSender<Message> alarmSender;
    private final AlarmRepository alarmRepository;

    @Override
    public void send(Member member, AlarmRequestDTO.SendAlarm... request) {
        // 알림 만들기
        List<Alarm> alarms = new ArrayList<>();
        for (AlarmRequestDTO.SendAlarm req : request) {
            try {
                alarmSender.send(member, req);

                alarms.add(AlarmConverter.toAlarm(member, req));
            } catch (Exception ignored){}
        }
        alarmRepository.saveAll(alarms);
    }
}
