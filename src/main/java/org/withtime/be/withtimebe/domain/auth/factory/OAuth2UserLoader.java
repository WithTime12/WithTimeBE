package org.withtime.be.withtimebe.domain.auth.factory;

import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;

public interface OAuth2UserLoader {
    OAuth2ResponseDTO.GetUserInfo loadUser(String code);
    String getSocialType();
}
