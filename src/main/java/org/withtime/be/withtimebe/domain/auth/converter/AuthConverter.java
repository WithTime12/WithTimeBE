package org.withtime.be.withtimebe.domain.auth.converter;

import org.withtime.be.withtimebe.domain.auth.dto.response.AuthResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.enums.ProviderType;

public class AuthConverter {

    public static AuthResponseDTO.Login toLoginResponse(String accessToken, String refreshToken) {
        return AuthResponseDTO.Login.builder()
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
