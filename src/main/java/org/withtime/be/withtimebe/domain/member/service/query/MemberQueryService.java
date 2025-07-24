package org.withtime.be.withtimebe.domain.member.service.query;

import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface MemberQueryService {
    Member findById(Long id);
}
