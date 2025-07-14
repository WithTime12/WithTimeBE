package org.withtime.be.withtimebe.domain.auth.factory.support;

import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.enums.SocialType;

@Component
public class KakaoUserLoader extends AbstractOAuth2UserLoader {


    @Override
    protected String getAccessToken(String code) {
        return "";
    }

    @Override
    protected OAuth2ResponseDTO.GetUserInfo getUserInfo(String token) {
        return null;
    }

    @Override
    public String getSocialType() {
        return SocialType.KAKAO.name();
    }
}
