package org.withtime.be.withtimebe.domain.auth.dto.request;


import jakarta.validation.constraints.Email;

public record EmailRequestDTO() {

    public record Send(
            @Email
            String email
    ) {
    }

    public record Check(
            @Email
            String email,
            String code
    ) {
    }
}
