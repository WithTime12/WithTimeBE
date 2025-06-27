package org.withtime.be.withtimebe.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.auth.converter.AuthConverter;
import org.withtime.be.withtimebe.domain.auth.dto.request.AuthRequest;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public void signUp(AuthRequest.SignUpRequest request) {
        if (availableSignUp(request)) {
            Member member = AuthConverter.toLocalMember(request.getUsername(), passwordEncoder.encode(request.getPassword()));
            memberRepository.save(member);
        }
    }

    private boolean availableSignUp(AuthRequest.SignUpRequest request) {
        // TODO: 회원가입 조건
        return true;
    }
}
