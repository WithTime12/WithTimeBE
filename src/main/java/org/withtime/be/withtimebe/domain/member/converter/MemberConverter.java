package org.withtime.be.withtimebe.domain.member.converter;

import org.withtime.be.withtimebe.domain.member.dto.MemberResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.enums.Grade;

public class MemberConverter {

    public static MemberResponseDTO.ChangeInfo toChangeInfo(Member member) {
        return MemberResponseDTO.ChangeInfo.builder()
                .username(member.getUsername())
                .build();
    }

    public static MemberResponseDTO.FindMyGrade toFindMyGrade(Member member) {
        Grade grade = Grade.fromPoint(member.getPoint());
        Integer nextRequiredPoint = Grade.nextRequiredPoint(member.getPoint());

        return MemberResponseDTO.FindMyGrade.builder()
            .level(grade.getLevel())
            .description(grade.getDescription())
            .nextRequiredPoint(nextRequiredPoint)
            .build();
    }
}
