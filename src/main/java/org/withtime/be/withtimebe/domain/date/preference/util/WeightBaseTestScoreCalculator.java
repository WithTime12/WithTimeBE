package org.withtime.be.withtimebe.domain.date.preference.util;

import org.springframework.stereotype.Component;
import org.withtime.be.withtimebe.domain.date.preference.converter.DatePreferenceConverter;
import org.withtime.be.withtimebe.domain.date.preference.dto.DatePreferenceRequestDTO;
import org.withtime.be.withtimebe.domain.date.preference.entity.DatePreferenceTestResult;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferencePartType;
import org.withtime.be.withtimebe.domain.date.preference.entity.enums.PreferenceType;

import java.util.List;

@Component
public class WeightBaseTestScoreCalculator implements DatePreferenceTestScoreCalculator {

    private static final PreferencePartType[][] types = {
            {PreferencePartType.S, PreferencePartType.F},
            {PreferencePartType.A, PreferencePartType.C},
            {PreferencePartType.M, PreferencePartType.P},
            {PreferencePartType.E, PreferencePartType.O}
    };

    private static final double[] weights = {
            1, 1, 1, 1, 1.1, 1, 1.1, 1, 1.1, 1,
            1, 1.1, 1.1, 1, 1, 1, 1.1, 1, 1, 1,
            1.1, 1, 1, 1, 1.1, 1, 1, 1, 1.1, 1,
            1.1, 1, 1, 1.1, 1, 1, 1, 1, 1.1, 1
    };

    @Override
    public DatePreferenceTestResult calculateTestScore(DatePreferenceRequestDTO.Test request) {
        List<Integer> answers = request.answers(); // 정답 배열
        int number = types.length; // PartType 개수
        int size = answers.size() / number; // PartType 개수 별 정답 개수
        Double[] percentages = new Double[number];
        StringBuilder type = new StringBuilder();

        // 계산
        for (int i = 0; i < number ; i++) {
            type.append(calculatePartType(answers, percentages, i * size, size));
        }

        return DatePreferenceConverter.toDatePreferenceTestResult(
                PreferenceType.valueOf(type.toString()),
                percentages[0],
                percentages[1],
                percentages[2],
                percentages[3]
        );
    }

    private String calculatePartType(List<Integer> list, Double[] percentages, int start, int size) {
        double score = Math.floor((calculateScore(list, start, size) - 1) * 1000) / 10;
        int index = start / size;
        if (score < 50.0) {
            percentages[index] = 100.0 - score;
            return types[index][0].name();
        }
        else {
            percentages[index] = score;
            return types[index][1].name();
        }
    }

    // return value between 1, 2
    private double calculateScore(List<Integer> list, int start, int size) {
        int end = start + size;
        double sum = 0.0;
        for (int i = start; i < end; i++) {
            sum += list.get(i) * weights[i];
        }
        return sum / size;
    }
}
