package org.withtime.be.withtimebe.domain.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface MemberQueryService {
    Member findById(Long id);

    Page<Member> findMemberList(Pageable pageable);
}
