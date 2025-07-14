package org.withtime.be.withtimebe.domain.auth.factory.support;

import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.auth.factory.OAuth2UserLoader;
import org.withtime.be.withtimebe.global.error.code.OAuthErrorCode;
import org.withtime.be.withtimebe.global.error.exception.OAuthException;

public abstract class AbstractOAuth2UserLoader implements OAuth2UserLoader {

    @Override
    public OAuth2ResponseDTO.GetUserInfo loadUser(String code) {
        try {
            String token = getAccessToken(code);
            return getUserInfo(token);
        }
        catch (Exception e) {
            throw new OAuthException(OAuthErrorCode.FAIL_TO_GET_USER_INFO);
        }
    }

    protected abstract String getAccessToken(String code);

    protected abstract OAuth2ResponseDTO.GetUserInfo getUserInfo(String token);

}
