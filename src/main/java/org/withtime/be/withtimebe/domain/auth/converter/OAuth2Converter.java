package org.withtime.be.withtimebe.domain.auth.converter;

import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.auth.factory.support.dto.KakaoOAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.auth.factory.support.dto.NaverOAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.enums.SocialType;

public class OAuth2Converter {
    public static OAuth2ResponseDTO.Login toLogin(String email, boolean isFirst, Long socialId) {
        return OAuth2ResponseDTO.Login.builder()
                .email(email)
                .socialId(socialId)
                .isFirst(isFirst)
                .build();
    }

    public static OAuth2ResponseDTO.GetUserInfo toGetUserInfo(KakaoOAuth2ResponseDTO.KakaoProfile kakaoProfile) {
        return OAuth2ResponseDTO.GetUserInfo.builder()
                .email(kakaoProfile.kakao_account().email())
                .providerId(String.valueOf(kakaoProfile.id()))
                .socialType(SocialType.KAKAO)
                .build();
    }

    public static OAuth2ResponseDTO.GetUserInfo toGetUserInfo(NaverOAuth2ResponseDTO.UserInfo.UserInfoData naver) {
        return OAuth2ResponseDTO.GetUserInfo.builder()
                .email(naver.email())
                .providerId(naver.id())
                .socialType(SocialType.NAVER)
                .build();
    }
}
