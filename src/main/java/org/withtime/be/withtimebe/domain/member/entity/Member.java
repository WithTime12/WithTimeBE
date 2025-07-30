package org.withtime.be.withtimebe.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.withtime.be.withtimebe.domain.member.entity.enums.Gender;
import org.withtime.be.withtimebe.domain.member.entity.enums.Role;
import org.withtime.be.withtimebe.domain.member.entity.enums.UserRank;
import org.withtime.be.withtimebe.global.common.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_rank", nullable = false)
    private UserRank userRank;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_auto_payment", nullable = false)
    private Boolean isAutoPayment;

    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "push_alarm", nullable = false)
    @Builder.Default
    private Boolean pushAlarm = true;

    @Column(name = "email_alarm", nullable = false)
    @Builder.Default
    private Boolean emailAlarm = true;

    @Column(name = "sms_alarm", nullable = false)
    @Builder.Default
    private Boolean smsAlarm = true;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public void changeUsername(String newUsername) {
        this.username= newUsername;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void updateAlarmSetting(Boolean pushAlarm, Boolean emailAlarm, Boolean smsAlarm) {
        this.pushAlarm = pushAlarm;
        this.emailAlarm = emailAlarm;
        this.smsAlarm = smsAlarm;
    }
}
