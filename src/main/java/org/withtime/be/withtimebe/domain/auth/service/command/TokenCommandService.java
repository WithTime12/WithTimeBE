package org.withtime.be.withtimebe.domain.auth.service.command;

import org.withtime.be.withtimebe.domain.auth.dto.response.AuthResponseDTO;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;

public interface TokenCommandService {
    AuthResponseDTO.Login createLoginToken(CustomUserDetails customUserDetails);
}
