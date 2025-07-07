package org.withtime.be.withtimebe.domain.auth.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.auth.converter.AuthConverter;
import org.withtime.be.withtimebe.domain.auth.dto.request.AuthRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public void signUp(AuthRequestDTO.SignUp request) {
        if (availableSignUp(request)) {
            Member member = AuthConverter.toLocalMember(request.email(), request.username(), passwordEncoder.encode(request.password()), request.phoneNumber(), request.gender(), request.birth());
            memberRepository.save(member);
        }
    }

    private boolean availableSignUp(AuthRequestDTO.SignUp request) {
        return !memberRepository.existsByEmail(request.email());
    }
}
