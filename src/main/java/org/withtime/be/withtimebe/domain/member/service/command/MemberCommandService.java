package org.withtime.be.withtimebe.domain.member.service.command;

import org.withtime.be.withtimebe.domain.member.dto.request.MemberRequestDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public interface MemberCommandService {

	Member updateMembership(MemberRequestDTO.UpdateMembership request);
}
