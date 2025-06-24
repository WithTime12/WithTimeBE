package org.withtime.be.withtimebe.domain.auth.converter;

import org.withtime.be.withtimebe.domain.auth.dto.response.AuthResponse;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.enums.ProviderType;

public class AuthConverter {

    public static AuthResponse.LoginResponse toLoginResponse(String accessToken, String refreshToken) {
        return AuthResponse.LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static Member toLocalMember(String username, String encodedPassword) {
        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .providerType(ProviderType.LOCAL)
                .build();
    }
}
