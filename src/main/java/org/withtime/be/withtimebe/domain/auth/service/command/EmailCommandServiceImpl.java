package org.withtime.be.withtimebe.domain.auth.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.withtime.be.withtimebe.domain.auth.dto.request.EmailRequestDTO;
import org.withtime.be.withtimebe.domain.auth.generator.RandomGenerator;
import org.withtime.be.withtimebe.domain.auth.service.query.EmailVerificationCodeStorageQueryService;
import org.withtime.be.withtimebe.domain.auth.util.MailVerificationCodeSender;
import org.withtime.be.withtimebe.global.error.code.EmailErrorCode;
import org.withtime.be.withtimebe.global.error.exception.EmailException;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailCommandServiceImpl implements EmailCommandService {

    private final RandomGenerator<String> randomSixDigitGenerator;
    private final MailVerificationCodeSender mailVerificationCodeSender;
    private final EmailVerificationCodeStorageCommandService emailVerificationCodeStorageCommandService;
    private final EmailVerificationCodeStorageQueryService emailVerificationCodeStorageQueryService;

    @Override
    public void sendEmail(EmailRequestDTO.Send request) {
        String email = request.email();
        String code = randomSixDigitGenerator.generateRandom();

        emailVerificationCodeStorageCommandService.saveVerificationCode(email, code);
        try {
            mailVerificationCodeSender.sendMail(email, code);
        } catch (Exception e) {
            emailVerificationCodeStorageCommandService.deleteVerificationCode(email);
            throw e;
        }

    }

    @Override
    public void checkEmail(EmailRequestDTO.Check request) {
        String email = request.email();
        if (emailVerificationCodeStorageQueryService.checkVerificationCode(email, request.code())) {
            emailVerificationCodeStorageCommandService.saveVerifiedEmail(email);
        }
        else {
            throw new EmailException(EmailErrorCode.INCORRECT_EMAIL_VERIFICATION_CODE);
        }
        emailVerificationCodeStorageCommandService.deleteVerificationCode(email);
    }
}
