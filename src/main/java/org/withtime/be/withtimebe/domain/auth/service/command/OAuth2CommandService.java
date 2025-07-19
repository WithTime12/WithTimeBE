package org.withtime.be.withtimebe.domain.auth.service.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.withtime.be.withtimebe.domain.auth.dto.response.OAuth2ResponseDTO;

public interface OAuth2CommandService {
    OAuth2ResponseDTO.Login login(HttpServletRequest request, HttpServletResponse response, String provider, String code);
}
