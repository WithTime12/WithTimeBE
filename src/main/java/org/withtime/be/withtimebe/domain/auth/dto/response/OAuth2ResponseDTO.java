package org.withtime.be.withtimebe.domain.auth.dto.response;

import lombok.Builder;
import org.withtime.be.withtimebe.domain.member.entity.enums.SocialType;

public record OAuth2ResponseDTO() {

    @Builder
    public record Login(
            String email,
            Long socialId,
            boolean isFirst
    ) {

    }

    @Builder
    public record GetUserInfo(
            String email,
            String providerId,
            SocialType socialType
    ) {

    }

}
