package org.withtime.be.withtimebe.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.code.DefaultResponseErrorCode;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.namul.api.payload.writer.FailureResponseWriter;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final FailureResponseWriter<DefaultResponseErrorReasonDTO> failureResponseWriter;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper om = new ObjectMapper();
        DefaultResponseErrorReasonDTO reasonDTO = DefaultResponseErrorCode._UNAUTHORIZED.getReason();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(reasonDTO.getHttpStatus().value());
        om.writeValue(response.getOutputStream(), failureResponseWriter.onFailure(reasonDTO, authException.getMessage()));
    }
}
