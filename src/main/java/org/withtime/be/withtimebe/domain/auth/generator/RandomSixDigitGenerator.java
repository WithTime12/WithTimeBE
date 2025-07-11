package org.withtime.be.withtimebe.domain.auth.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class RandomSixDigitGenerator implements RandomGenerator<Integer> {

    private static final Random RANDOM = new SecureRandom();

    @Override
    public Integer generateRandom() {
        return RANDOM.nextInt(1000000);
    }
}
