package org.withtime.be.withtimebe.domain.member.converter;

import org.withtime.be.withtimebe.domain.member.dto.MemberResponseDTO;
import org.withtime.be.withtimebe.domain.member.entity.Member;

public class MemberConverter {

    public static MemberResponseDTO.ChangeInfo toChangeInfo(Member member) {
        return MemberResponseDTO.ChangeInfo.builder()
                .username(member.getUsername())
                .build();
    }

    public static MemberResponseDTO.MemberInfo toMemberInfo(Member member) {
        return MemberResponseDTO.MemberInfo.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .userRank(member.getUserRank())
                .phoneNumber(member.getPhoneNumber())
                .isAuthPayment(member.getIsAutoPayment())
                .gender(member.getGender())
                .birth(member.getBirth())
                .role(member.getRole())
                .point(member.getPoint())
                .build();
    }
}
