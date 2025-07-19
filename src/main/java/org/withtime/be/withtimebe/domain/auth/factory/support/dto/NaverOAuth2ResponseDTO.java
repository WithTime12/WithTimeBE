package org.withtime.be.withtimebe.domain.auth.factory.support.dto;

public record NaverOAuth2ResponseDTO() {
    public record Token(
            String access_token,
            String refresh_token,
            String token_type,
            String expires_in
    ) {
    }

    public record UserInfo(
            String resultcode,
            String message,
            UserInfoData response
    ) {
        public record UserInfoData(
                String id,
                String email
        ) {

        }
    }
}
