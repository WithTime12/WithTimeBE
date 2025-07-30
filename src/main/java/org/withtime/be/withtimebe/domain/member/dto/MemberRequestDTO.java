package org.withtime.be.withtimebe.domain.member.dto;

public record MemberRequestDTO() {

    public record ChangePassword(
            String nowPassword,
            String newPassword
    ) {

    }

    public record ChangeInfo(
            String username
    ) {

    }
}
