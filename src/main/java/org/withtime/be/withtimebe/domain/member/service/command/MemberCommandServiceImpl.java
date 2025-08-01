package org.withtime.be.withtimebe.domain.member.service.command;

import lombok.RequiredArgsConstructor;
import org.namul.api.payload.error.exception.ServerApplicationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.member.dto.MemberRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;
import org.withtime.be.withtimebe.global.error.code.AuthErrorCode;
import org.withtime.be.withtimebe.global.error.code.MemberErrorCode;
import org.withtime.be.withtimebe.global.error.exception.AuthException;
import org.withtime.be.withtimebe.global.error.exception.MemberException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;


    @Override
    public void changePassword(Member member, MemberRequestDTO.ChangePassword request) {
       if (member.getPassword() != null && !passwordEncoder.matches(request.nowPassword(), member.getPassword())) {
           throw new AuthException(AuthErrorCode.INCORRECT_PASSWORD);
       }
       this.changePassword(member.getEmail(), request.newPassword());
    }

    @Override
    public void changePassword(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND));
        validateChangePassword(member, password);
        member.changePassword(passwordEncoder.encode(password));
    }

    @Override
    public Member changeInfo(Long memberId, MemberRequestDTO.ChangeInfo request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new MemberException(MemberErrorCode.NOT_FOUND));
        member.changeUsername(request.username());
        return member;
    }

    @Override
    public void addPoint(Long memberId, Integer point) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
            new MemberException(MemberErrorCode.NOT_FOUND));
        member.addPoint(point);
    }

    public void validateChangePassword(Member member, String password) throws ServerApplicationException {
        String memberPassword = member.getPassword();
        if (memberPassword == null) {
            throw new AuthException(AuthErrorCode.ONLY_AVAILABLE_SOCIAL);
        }
        else if (passwordEncoder.matches(password, memberPassword)) {
            throw new MemberException(MemberErrorCode.SAME_PASSWORD);
        }
    }
}
