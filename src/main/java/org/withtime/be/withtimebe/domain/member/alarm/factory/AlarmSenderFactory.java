package org.withtime.be.withtimebe.domain.member.alarm.factory;

import com.google.firebase.messaging.Message;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.member.alarm.service.AlarmSender;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AlarmSenderFactory {

    private static final Map<Class<?>, AlarmSender<?>> alarmSenderRepository = new ConcurrentHashMap<>();

    public AlarmSenderFactory(@NotNull List<AlarmSender<?>> alarmSenders) {
        alarmSenders.forEach(sender -> alarmSenderRepository.put(sender.supportedClass(), sender));
    }

    public <T> AlarmSender<T> getAlarmSender(Class<T> messageType) {
        try {
            @SuppressWarnings("unchecked")
            AlarmSender<T> alarmSender = (AlarmSender<T>) alarmSenderRepository.get(messageType);
            return alarmSender;
        } catch (Exception e) {
            return null;
        }
    }

    public Class<?> getPushAlarmClass() {
        return Message.class;
    }

    public Class<?> getEmailAlarmClass() {
        return MimeMessage.class;
    }

    public Class<?> getSMSAlarmClass() {
        return String.class;
    }
}
