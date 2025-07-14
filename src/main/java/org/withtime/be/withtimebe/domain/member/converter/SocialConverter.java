package org.withtime.be.withtimebe.domain.member.converter;

import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.Social;

public class SocialConverter {
    public static Social toSocial(OAuth2ResponseDTO.GetUserInfo userInfo) {
        return Social.builder()
                .socialType(userInfo.socialType())
                .providerId(userInfo.providerId())
                .build();
    }

    public static Social toSocial(OAuth2ResponseDTO.GetUserInfo userInfo, Member member) {
        return Social.builder()
                .socialType(userInfo.socialType())
                .providerId(userInfo.providerId())
                .member(member)
                .build();
    }
}
