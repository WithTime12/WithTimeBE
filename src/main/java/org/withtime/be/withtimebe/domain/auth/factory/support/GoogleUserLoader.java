package org.withtime.be.withtimebe.domain.auth.factory.support;

import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.auth.converter.OAuth2Converter;
import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.auth.factory.support.dto.GoogleOAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.enums.SocialType;
import org.withtime.be.withtimebe.global.data.OAuth2ConfigData;

import java.io.IOException;

@Component
public class GoogleUserLoader extends AbstractOAuth2UserLoader {

    private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    private static final SocialType SOCIAL_TYPE = SocialType.GOOGLE;

    public GoogleUserLoader(OAuth2ConfigData oAuth2ConfigData) {
        super(oAuth2ConfigData);
    }

    @Override
    protected String getAccessToken(String code) throws IOException {
        GoogleOAuth2ResponseDTO.Token token = super.getToken(code, GoogleOAuth2ResponseDTO.Token.class);
        return token.access_token();
    }

    @Override
    protected OAuth2ResponseDTO.GetUserInfo getUserInfo(String token) throws IOException {
        GoogleOAuth2ResponseDTO.UserInfo userInfo = super.getProfile(AUTHORIZATION_TOKEN_PREFIX, token, GoogleOAuth2ResponseDTO.UserInfo.class);
        return OAuth2Converter.toGetUserInfo(userInfo);
    }

    @Override
    public String getSocialType() {
        return SOCIAL_TYPE.name().toLowerCase();
    }

}
