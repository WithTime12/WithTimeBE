package org.withtime.be.withtimebe.domain.auth.service.command;

import org.withtime.be.withtimebe.domain.auth.dto.request.EmailRequestDTO;

public interface EmailCommandService {
    void sendEmail(EmailRequestDTO.Send request);
    void checkEmail(EmailRequestDTO.Check request);
}
