package org.withtime.be.withtimebe.global.error;

import lombok.RequiredArgsConstructor;
import org.namul.api.payload.code.DefaultResponseErrorCode;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.namul.api.payload.error.configurer.DefaultExceptionAdviceConfigurer;
import org.namul.api.payload.error.configurer.ExceptionAdviceConfigurer;
import org.namul.api.payload.writer.FailureResponseWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Configuration
@RequiredArgsConstructor
public class ExceptionAdviceConfig {

    private final NoResourceFoundExceptionHandler noResourceFoundExceptionHandler;
    @Bean
    ExceptionAdviceConfigurer<DefaultResponseErrorReasonDTO> defaultExceptionAdviceConfigurer(FailureResponseWriter<DefaultResponseErrorReasonDTO> failureResponseWriter) {
        ExceptionAdviceConfigurer<DefaultResponseErrorReasonDTO> configurer = new DefaultExceptionAdviceConfigurer(failureResponseWriter);
        configurer.addAdvice(NoResourceFoundException.class, noResourceFoundExceptionHandler, DefaultResponseErrorCode._BAD_REQUEST.getReason());
        return configurer;
    }
}
