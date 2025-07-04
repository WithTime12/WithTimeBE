package org.withtime.be.withtimebe.domain.auth.dto.request;

public record AuthRequestDTO() {

    public record Login(
            String username,
            String password
    ) {

    }

    public record SignUp(
            String username,
            String password
    ) {

    }
}
