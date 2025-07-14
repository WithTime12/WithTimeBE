package org.withtime.be.withtimebe.domain.auth.converter;

import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;

public class OAuth2Converter {
    public static OAuth2ResponseDTO.Login toLogin(String email, boolean isFirst, Long socialId) {
        return OAuth2ResponseDTO.Login.builder()
                .email(email)
                .socialId(socialId)
                .isFirst(isFirst)
                .build();
    }
}
