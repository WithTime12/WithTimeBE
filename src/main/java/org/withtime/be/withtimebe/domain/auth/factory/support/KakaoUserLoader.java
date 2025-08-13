package org.withtime.be.withtimebe.domain.auth.factory.support;

import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.auth.converter.OAuth2Converter;
import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.auth.factory.support.dto.KakaoOAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.enums.SocialType;
import org.withtime.be.withtimebe.global.data.OAuth2ConfigData;

import java.io.IOException;

@Component
public class KakaoUserLoader extends AbstractOAuth2UserLoader {

    private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    private static final SocialType SOCIAL_TYPE = SocialType.KAKAO;

    public KakaoUserLoader(OAuth2ConfigData oAuth2ConfigData) {
        super(oAuth2ConfigData);
    }

    @Override
    protected String getAccessToken(String code) throws IOException {
        KakaoOAuth2ResponseDTO.Token oAuth2TokenDTO = getToken(code, KakaoOAuth2ResponseDTO.Token.class);
        return oAuth2TokenDTO.access_token();
    }

    @Override
    protected OAuth2ResponseDTO.GetUserInfo getUserInfo(String token) throws IOException {
        KakaoOAuth2ResponseDTO.KakaoProfile kakaoProfile = super.getProfile(AUTHORIZATION_TOKEN_PREFIX, token, KakaoOAuth2ResponseDTO.KakaoProfile.class);
        return OAuth2Converter.toGetUserInfo(kakaoProfile);
    }


    @Override
    public String getSocialType() {
        return SOCIAL_TYPE.name().toLowerCase();
    }

}
