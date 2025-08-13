package org.withtime.be.withtimebe.domain.member.alarm.generator;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.member.dto.AlarmRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

@Component
@Transactional
public class FCMAlarmMessageGenerator implements AlarmMessageGenerator<Message> {

    @Override
    public Message generate(Member member, AlarmRequestDTO.SendAlarm request) {
        return Message.builder()
                .setNotification(toNotification(request))
                .setToken(member.getDeviceToken())
                .build();
    }

    private Notification toNotification(AlarmRequestDTO.SendAlarm request) {
        return Notification.builder()
                .setTitle(request.title())
                .setBody(request.description())
                .build();
    }
}
