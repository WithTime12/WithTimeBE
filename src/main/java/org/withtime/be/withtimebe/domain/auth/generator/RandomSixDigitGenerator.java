package org.withtime.be.withtimebe.domain.auth.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class RandomSixDigitGenerator implements RandomGenerator<String> {

    private static final Random RANDOM = new SecureRandom();

    @Override
    public String generateRandom() {
        return String.format("%06d", RANDOM.nextInt(1000000));
    }
}
