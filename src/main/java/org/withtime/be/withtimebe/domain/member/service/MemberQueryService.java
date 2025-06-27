package org.withtime.be.withtimebe.domain.member.service;

import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface MemberQueryService {
    Member findById(Long id);
}
