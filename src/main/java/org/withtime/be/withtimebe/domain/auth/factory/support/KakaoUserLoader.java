package org.withtime.be.withtimebe.domain.auth.factory.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.withtime.be.withtimebe.domain.auth.converter.OAuth2Converter;
import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.auth.factory.support.dto.KakaoOAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.enums.SocialType;
import org.withtime.be.withtimebe.global.data.OAuth2ConfigData;

import java.io.IOException;

@Component
public class KakaoUserLoader extends AbstractOAuth2UserLoader {

    private final SocialType socialType = SocialType.KAKAO;

    public KakaoUserLoader(OAuth2ConfigData oAuth2ConfigData) {
        super(oAuth2ConfigData);
    }

    @Override
    protected String getAccessToken(String code) throws IOException {
        KakaoOAuth2ResponseDTO.Token oAuth2TokenDTO = getToken(code);
        return oAuth2TokenDTO.access_token();
    }

    @Override
    protected OAuth2ResponseDTO.GetUserInfo getUserInfo(String token) throws IOException {
        KakaoOAuth2ResponseDTO.KakaoProfile kakaoProfile = getKakaoProfile(token);
        return OAuth2Converter.toGetUserInfo(kakaoProfile);
    }


    @Override
    public String getSocialType() {
        return this.socialType.name().toLowerCase();
    }

    private KakaoOAuth2ResponseDTO.Token getToken(String code) throws IOException {
        // 인가코드 토큰 가져오기
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", getClientId());
        map.add("redirect_uri", getRedirectUri());
        map.add("code", code);
        HttpEntity<MultiValueMap> request = new HttpEntity<>(map, httpHeaders);

        ResponseEntity<String> response1 = restTemplate.exchange(
                getTokenUri(),
                HttpMethod.POST,
                request,
                String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoOAuth2ResponseDTO.Token oAuth2TokenDTO = null;

        return objectMapper.readValue(response1.getBody(), KakaoOAuth2ResponseDTO.Token.class);
    }

    private KakaoOAuth2ResponseDTO.KakaoProfile getKakaoProfile(String token) throws IOException{
        // 토큰으로 정보 가져오기
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Bearer " + token);
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap> request1 = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response2 = restTemplate.exchange(
                getUserInfoUri(),
                HttpMethod.GET,
                request1,
                String.class
        );

        ObjectMapper om = new ObjectMapper();

        return om.readValue(response2.getBody(), KakaoOAuth2ResponseDTO.KakaoProfile.class);
    }
}
