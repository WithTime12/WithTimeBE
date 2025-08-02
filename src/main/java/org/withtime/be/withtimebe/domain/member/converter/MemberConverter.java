package org.withtime.be.withtimebe.domain.member.converter;

import org.withtime.be.withtimebe.domain.member.dto.MemberResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.entity.enums.GradeType;

public class MemberConverter {

    public static MemberResponseDTO.ChangeInfo toChangeInfo(Member member) {
        return MemberResponseDTO.ChangeInfo.builder()
                .username(member.getUsername())
                .build();
    }

    public static MemberResponseDTO.FindMyGrade toFindMyGrade(Member member) {
        GradeType gradeType = GradeType.fromPoint(member.getPoint());
        Integer nextRequiredPoint = GradeType.nextRequiredPoint(member.getPoint());

        return MemberResponseDTO.FindMyGrade.builder()
            .username(member.getUsername())
            .grade(gradeType.name())
            .level(gradeType.getLevel())
            .description(gradeType.getDescription())
            .nextRequiredPoint(nextRequiredPoint)
            .build();
    }
}
