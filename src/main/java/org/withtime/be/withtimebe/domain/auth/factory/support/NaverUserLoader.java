package org.withtime.be.withtimebe.domain.auth.factory.support;

import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.auth.converter.OAuth2Converter;
import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.auth.factory.support.dto.NaverOAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.enums.SocialType;
import org.withtime.be.withtimebe.global.data.OAuth2ConfigData;

import java.io.IOException;

@Component
public class NaverUserLoader extends AbstractOAuth2UserLoader {

    private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    private static final SocialType SOCIAL_TYPE = SocialType.NAVER;

    public NaverUserLoader(OAuth2ConfigData oAuth2ConfigData) {
        super(oAuth2ConfigData);
    }

    @Override
    protected String getAccessToken(String code) throws IOException {
        NaverOAuth2ResponseDTO.Token oAuth2TokenDTO = getToken(code, NaverOAuth2ResponseDTO.Token.class);
        return oAuth2TokenDTO.access_token();
    }

    @Override
    protected OAuth2ResponseDTO.GetUserInfo getUserInfo(String token) throws IOException {
        NaverOAuth2ResponseDTO.UserInfo.UserInfoData userInfo = super.getProfile(AUTHORIZATION_TOKEN_PREFIX, token, NaverOAuth2ResponseDTO.UserInfo.class).response();
        return OAuth2Converter.toGetUserInfo(userInfo);
    }

    @Override
    public String getSocialType() {
        return SOCIAL_TYPE.name().toLowerCase();
    }
}
