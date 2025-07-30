package org.withtime.be.withtimebe.domain.member.alarm.sender;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.member.entity.Member;

@Component
public class FCMAlarmSendUtil implements AlarmSendUtil<Message> {

    @Override
    public void send(Member member, Message message) throws Exception {
        FirebaseMessaging.getInstance().send(message);
    }
}
