package org.withtime.be.withtimebe.domain.auth.factory.support.dto;

public record GoogleOAuth2ResponseDTO() {
    public record Token(
            String access_token,
            String refresh_token,
            Long expires_in,
            String token_type,
            String scope,
            String id_token
    ) {

    }

    public record UserInfo(
            String id,
            String email,
            Boolean verified_email,
            String picture
    ) {

    }
}
