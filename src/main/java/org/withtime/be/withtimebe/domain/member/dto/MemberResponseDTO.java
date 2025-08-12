package org.withtime.be.withtimebe.domain.member.dto;

import lombok.Builder;
import org.withtime.be.withtimebe.domain.member.entity.enums.Gender;
import org.withtime.be.withtimebe.domain.member.entity.enums.Role;
import org.withtime.be.withtimebe.domain.member.entity.enums.UserRank;

import java.time.LocalDate;

public record MemberResponseDTO() {
    @Builder
    public record ChangeInfo(
            String username
    ) {}

    @Builder
    public record MemberInfo(
            Long id,
            String email,
            String username,
            UserRank userRank,
            String phoneNumber,
            Boolean isAuthPayment,
            Gender gender,
            LocalDate birth,
            Role role,
            Integer point
    ) {

    }
}
