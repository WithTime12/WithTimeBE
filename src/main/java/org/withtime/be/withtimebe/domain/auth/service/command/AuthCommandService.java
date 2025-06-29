package org.withtime.be.withtimebe.domain.auth.service.command;

import org.withtime.be.withtimebe.domain.auth.dto.request.AuthRequestDTO;

public interface AuthCommandService {
    void signUp(AuthRequestDTO.SignUp request);
}
