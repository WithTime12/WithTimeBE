package org.withtime.be.withtimebe.domain.auth.dto.request;

import lombok.Getter;

public class AuthRequest {

    @Getter
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Getter
    public static class SignUpRequest {
        private String username;
        private String password;
    }
}
