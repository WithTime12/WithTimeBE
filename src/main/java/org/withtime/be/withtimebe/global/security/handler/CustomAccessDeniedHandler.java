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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final FailureResponseWriter<DefaultResponseErrorReasonDTO>failureResponseWriter;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper om = new ObjectMapper();
        DefaultResponseErrorReasonDTO reasonDTO = DefaultResponseErrorCode._FORBIDDEN.getReason();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(reasonDTO.getHttpStatus().value());
        om.writeValue(response.getOutputStream(), failureResponseWriter.onFailure(reasonDTO, accessDeniedException.getMessage()));
    }
}
