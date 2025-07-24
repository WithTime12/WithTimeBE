package org.withtime.be.withtimebe.domain.auth.service.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.auth.converter.AuthConverter;
import org.withtime.be.withtimebe.domain.auth.dto.request.AuthRequestDTO;
import org.withtime.be.withtimebe.domain.auth.service.query.EmailVerificationCodeStorageQueryService;
import org.withtime.be.withtimebe.domain.auth.service.query.TokenQueryService;
import org.withtime.be.withtimebe.domain.auth.service.query.TokenStorageQueryService;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.Social;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;
import org.withtime.be.withtimebe.domain.member.repository.SocialRepository;
import org.withtime.be.withtimebe.global.error.code.*;
import org.withtime.be.withtimebe.global.error.exception.*;
import org.withtime.be.withtimebe.global.security.constants.AuthenticationConstants;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;
import org.withtime.be.withtimebe.global.util.CookieUtil;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandServiceImpl implements AuthCommandService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final SocialRepository socialRepository;
    private final TokenCommandService tokenCommandService;
    private final TokenStorageCommandService tokenStorageCommandService;
    private final TokenQueryService tokenQueryService;
    private final TokenStorageQueryService tokenStorageQueryService;
    private final EmailVerificationCodeStorageQueryService emailVerificationCodeStorageQueryService;

    @Override
    public void signUp(AuthRequestDTO.SignUp request) {
        validateSignUp(request);

        Member member = memberRepository.save(AuthConverter.toLocalMember(request.email(), request.username(), request.socialId() != null ? passwordEncoder.encode(request.password()) : null, request.phoneNumber(), request.gender(), request.birth()));
        if (request.socialId() != null) {
            Social social = socialRepository.findById(request.socialId()).orElseThrow(() ->
                    new SocialException(SocialErrorCode.NOT_FOUND_SOCIAL));
            social.addMember(member);
        }
    }

    @Override
    public void reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshToken(request);
        Long userId = getUserId(refreshToken);
        if (userId == null || !tokenStorageQueryService.getRefreshToken(userId).equals(refreshToken)) {
            throw new TokenException(TokenErrorCode.INVALID_REFRESH_TOKEN);
        }

        // Access Token이 있다면 BlackList 처리
        String accessToken = getAccessToken(request);
        if (accessToken != null) {
            tokenStorageCommandService.addBlackList(accessToken);
        }

        reissueTokenInCookie(request, response, userId);
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = getAccessToken(request);
        String refreshToken = getRefreshToken(request);

        tokenStorageCommandService.addBlackList(accessToken);
        tokenStorageCommandService.addBlackList(refreshToken);

        // 쿠키의 Refresh Token이 다른 경우를 대비해 Redis Refresh도 Black list 처리
        Long userId = getUserId(refreshToken);
        tokenStorageCommandService.addBlackList(tokenStorageQueryService.getRefreshToken(userId));
        tokenStorageCommandService.deleteRefreshToken(userId);

        CookieUtil.deleteCookie(request, response, AuthenticationConstants.ACCESS_TOKEN_NAME);
        CookieUtil.deleteCookie(request, response, AuthenticationConstants.REFRESH_TOKEN_NAME);
    }

    private void reissueTokenInCookie(HttpServletRequest request, HttpServletResponse response, Long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND)
        );
        String newAccessToken = tokenCommandService.reissueAccessToken(new CustomUserDetails(member));
        CookieUtil.addCookie(request, response, AuthenticationConstants.ACCESS_TOKEN_NAME, newAccessToken, (int) tokenQueryService.getAccessTokenExpiration().toSeconds());
    }

    private void validateSignUp(AuthRequestDTO.SignUp request) throws AuthException {
        if (memberRepository.existsByEmail(request.email())) {
            throw new AuthException(AuthErrorCode.ALREADY_EXIST_EMAIL);
        }
        if (!emailVerificationCodeStorageQueryService.isVerified(request.email())) {
            throw new EmailException(EmailErrorCode.UNVERIFIED_EMAIL);
        }
    }

    private Long getUserId(String token) {
        return tokenQueryService.getUserId(token);
    }

    private String getAccessToken(HttpServletRequest request) {
        return CookieUtil.getCookie(request, AuthenticationConstants.ACCESS_TOKEN_NAME);
    }

    private String getRefreshToken(HttpServletRequest request) {
        return CookieUtil.getCookie(request, AuthenticationConstants.REFRESH_TOKEN_NAME);
    }
}
