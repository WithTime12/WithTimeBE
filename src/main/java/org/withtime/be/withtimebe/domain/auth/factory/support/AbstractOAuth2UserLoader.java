package org.withtime.be.withtimebe.domain.auth.factory.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.auth.factory.OAuth2UserLoader;
import org.withtime.be.withtimebe.domain.auth.factory.support.dto.GoogleOAuth2ResponseDTO;
import org.withtime.be.withtimebe.global.data.OAuth2ConfigData;
import org.withtime.be.withtimebe.global.error.code.OAuthErrorCode;
import org.withtime.be.withtimebe.global.error.exception.OAuthException;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractOAuth2UserLoader implements OAuth2UserLoader {

    private final OAuth2ConfigData oAuth2ConfigData;

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

    protected abstract String getAccessToken(String code) throws IOException;

    protected abstract OAuth2ResponseDTO.GetUserInfo getUserInfo(String token) throws IOException;

    protected String getClientId() {
        return this.oAuth2ConfigData.getRegistration().get(this.getSocialType()).getClientId();
    }

    protected String getClientSecret() {
        return this.oAuth2ConfigData.getRegistration().get(this.getSocialType()).getClientSecret();
    }

    protected String getRedirectUri() {
        return this.oAuth2ConfigData.getRegistration().get(this.getSocialType()).getRedirectUri();
    }

    protected String getTokenUri() {
        return this.oAuth2ConfigData.getProvider().get(this.getSocialType()).getTokenUri();
    }

    protected String getUserInfoUri() {
        return this.oAuth2ConfigData.getProvider().get(this.getSocialType()).getUserInfoUri();
    }
}
