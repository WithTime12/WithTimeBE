package org.withtime.be.withtimebe.domain.auth.service.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.auth.converter.OAuth2Converter;
import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.auth.factory.OAuth2UserLoader;
import org.withtime.be.withtimebe.domain.auth.factory.OAuth2UserLoaderFactory;
import org.withtime.be.withtimebe.domain.member.converter.SocialConverter;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.Social;
import org.withtime.be.withtimebe.domain.member.repository.SocialRepository;
import org.withtime.be.withtimebe.global.error.code.OAuthErrorCode;
import org.withtime.be.withtimebe.global.error.exception.OAuthException;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;
import org.withtime.be.withtimebe.global.security.manager.TokenManager;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2CommandServiceImpl implements OAuth2CommandService {

    private final OAuth2UserLoaderFactory oAuth2UserLoaderFactory;
    private final SocialRepository socialRepository;
    private final TokenManager tokenManager;

    @Override
    public OAuth2ResponseDTO.Login login(HttpServletRequest request, HttpServletResponse response, String provider, String code) {
        OAuth2UserLoader userLoader = oAuth2UserLoaderFactory.getUserLoader(provider);
        if (userLoader == null) {
            throw new OAuthException(OAuthErrorCode.UNSUPPORTED_SOCIAL_TYPE);
        }
        OAuth2ResponseDTO.GetUserInfo userInfo = userLoader.loadUser(code);
        return successfulOAuth2(request, response, userInfo);
    }

    private OAuth2ResponseDTO.Login successfulOAuth2(HttpServletRequest request, HttpServletResponse response, OAuth2ResponseDTO.GetUserInfo userInfo) {
        Optional<Social> socialOptional = socialRepository.findByProviderIdAndSocialType(userInfo.providerId(), userInfo.socialType());
        // 소셜로 첫 로그인
        if (socialOptional.isEmpty()) {
            Social social = socialRepository.save(SocialConverter.toSocial(userInfo));
            return OAuth2Converter.toLogin(userInfo.email(), true, social.getId());
        }
        // 소셜로 로그인 한 적은 있지만 사용자와 연결 X 즉, 최초 로그인 X
        else if (socialOptional.get().getMember() == null) {
            Social social = socialOptional.get();
            return OAuth2Converter.toLogin(userInfo.email(), true, social.getId());
        }
        // 소셜로 로그인 한 적도 있고 사용자와도 연결된 경우
        else {
            Social social = socialOptional.get();
            processToken(request, response, social.getMember());
            return OAuth2Converter.toLogin(userInfo.email(), false, social.getId());
        }
    }

    private void processToken(HttpServletRequest request, HttpServletResponse response, Member member) {
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        tokenManager.addToken(request, response, customUserDetails);
    }
}
