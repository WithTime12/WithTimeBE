package org.withtime.be.withtimebe.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import org.withtime.be.withtimebe.domain.member.entity.enums.Gender;

import java.time.LocalDate;

public record AuthRequestDTO() {

    public record Login(
            @Email
            String email,
            String password
    ) {

    }

    public record SignUp(
            @Email
            String email,
            String username,
            String password,
            Gender gender,
            String phoneNumber,
            LocalDate birth,
            Long socialId
    ) {

    }
}
