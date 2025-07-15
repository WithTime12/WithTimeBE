package org.withtime.be.withtimebe.domain.auth.service.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.auth.converter.OAuth2Converter;
import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;
import org.withtime.be.withtimebe.domain.auth.factory.OAuth2UserLoader;
import org.withtime.be.withtimebe.domain.auth.factory.OAuth2UserLoaderFactory;
import org.withtime.be.withtimebe.domain.member.converter.SocialConverter;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.Social;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;
import org.withtime.be.withtimebe.domain.member.repository.SocialRepository;
import org.withtime.be.withtimebe.global.error.code.OAuthErrorCode;
import org.withtime.be.withtimebe.global.error.exception.OAuthException;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;
import org.withtime.be.withtimebe.global.security.manager.TokenManager;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuth2CommandServiceImpl implements OAuth2CommandService {

    private final OAuth2UserLoaderFactory oAuth2UserLoaderFactory;
    private final SocialRepository socialRepository;
    private final MemberRepository memberRepository;
    private final TokenManager tokenManager;

    private final EmailVerificationCodeStorageCommandService emailVerificationCodeStorageCommandService;

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
        Optional<Member> memberOptional = memberRepository.findByEmail(userInfo.email());

        // 해당 이메일로 회원가입이 된 경우
        if (memberOptional.isPresent()) {
            // 소셜이 있으면 가져오고 아니면 새로 만들기
            Social social = socialOptional.orElseGet(() -> socialRepository.save(SocialConverter.toSocial(userInfo, memberOptional.get())));
            processLogin(request, response, memberOptional.get());
            return OAuth2Converter.toLogin(userInfo.email(), false, social.getId());
        }
        // 회원가입이 안 된 경우
        else {
            Social social = socialOptional.orElseGet(() -> socialRepository.save(SocialConverter.toSocial(userInfo)));
            emailVerificationCodeStorageCommandService.saveVerifiedEmail(userInfo.email());
            return OAuth2Converter.toLogin(userInfo.email(), true, social.getId());
        }
    }

    private void processLogin(HttpServletRequest request, HttpServletResponse response, Member member) {
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        tokenManager.addToken(request, response, customUserDetails);
    }
}
