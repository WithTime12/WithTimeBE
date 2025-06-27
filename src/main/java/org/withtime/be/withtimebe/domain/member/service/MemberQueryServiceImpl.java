package org.withtime.be.withtimebe.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.repository.MemberRepository;
import org.withtime.be.withtimebe.global.error.code.MemberErrorCode;
import org.withtime.be.withtimebe.global.error.exception.MemberException;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));
    }
}
