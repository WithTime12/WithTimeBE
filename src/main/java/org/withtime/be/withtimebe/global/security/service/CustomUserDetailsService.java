package org.withtime.be.withtimebe.global.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;
import org.withtime.be.withtimebe.global.error.code.MemberErrorCode;
import org.withtime.be.withtimebe.global.error.exception.MemberException;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
        return new CustomUserDetails(member);
    }
}
