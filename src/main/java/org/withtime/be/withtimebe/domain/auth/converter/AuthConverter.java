package org.withtime.be.withtimebe.domain.auth.converter;

import org.withtime.be.withtimebe.domain.auth.dto.response.AuthResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.enums.Gender;
import org.withtime.be.withtimebe.domain.member.entity.enums.Role;
import org.withtime.be.withtimebe.domain.member.entity.enums.UserRank;

import java.time.LocalDate;

public class AuthConverter {

    public static AuthResponseDTO.Login toLoginResponse(String accessToken, String refreshToken) {
        return AuthResponseDTO.Login.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static Member toLocalMember(String email, String username, String encodedPassword, String phoneNumber, Gender gender, LocalDate birth) {
        return Member.builder()
                .email(email)
                .username(username)
                .password(encodedPassword)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .birth(birth)
                .userRank(UserRank.COMMON)
                .isAutoPayment(false)
                .role(Role.USER)
                .build();
    }
}
