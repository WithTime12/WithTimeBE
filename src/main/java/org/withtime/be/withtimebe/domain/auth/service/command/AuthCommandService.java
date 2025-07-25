package org.withtime.be.withtimebe.domain.auth.service.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.withtime.be.withtimebe.domain.auth.dto.request.AuthRequestDTO;

public interface AuthCommandService {
    void signUp(AuthRequestDTO.SignUp request);
    void reissueToken(HttpServletRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
    void findPassword(@RequestBody AuthRequestDTO.FindPassword request);
}
