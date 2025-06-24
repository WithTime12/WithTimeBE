package org.withtime.be.withtimebe.domain.auth.service;

import org.withtime.be.withtimebe.domain.auth.dto.response.AuthResponse;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;

public interface TokenService {
    AuthResponse.LoginResponse createLoginToken(CustomUserDetails customUserDetails);
}
