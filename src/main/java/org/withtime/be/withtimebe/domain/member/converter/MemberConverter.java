package org.withtime.be.withtimebe.domain.member.converter;

import org.withtime.be.withtimebe.domain.member.dto.MemberResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public class MemberConverter {

    public static MemberResponseDTO.ChangeInfo toChangeInfo(Member member) {
        return MemberResponseDTO.ChangeInfo.builder()
                .username(member.getUsername())
                .build();
    }
}
