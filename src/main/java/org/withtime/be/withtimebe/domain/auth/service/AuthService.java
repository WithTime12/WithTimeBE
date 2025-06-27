package org.withtime.be.withtimebe.domain.auth.service;

import org.withtime.be.withtimebe.domain.auth.dto.request.AuthRequest;

public interface AuthService {
    void signUp(AuthRequest.SignUpRequest request);
}
