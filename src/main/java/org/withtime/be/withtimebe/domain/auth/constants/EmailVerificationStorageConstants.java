package org.withtime.be.withtimebe.domain.auth.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVerificationStorageConstants {

    public static final String VERIFICATION_CODE_PREFIX = "EMAIL-VERIFICATION-CODE:";
    public static final String EMAIL_VERIFICATION_PREFIX = "EMAIL-VERIFICATION:";
    public static final Duration VERIFICATION_CODE_DURATION = Duration.ofMinutes(3);
    public static final Duration EMAIL_VERIFICATION_DURATION = Duration.ofHours(1);
}
