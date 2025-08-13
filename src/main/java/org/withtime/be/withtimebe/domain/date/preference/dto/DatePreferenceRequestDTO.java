package org.withtime.be.withtimebe.domain.date.preference.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record DatePreferenceRequestDTO() {
    public record Test(
            @Schema(
                    description = "1 또는 2 중 하나의 값으로 이루어진 40개의 답변",
                    example = """
                    [
                        1, 1, 1, 1, 2, 2, 2, 2, 2, 2,
                        1, 1, 1, 1, 1, 1, 2, 2, 2, 2,
                        1, 1, 1, 1, 2, 2, 2, 2, 2, 2,
                        1, 1, 1, 1, 1, 1, 2, 2, 2, 2
                    ]
                    """
            )
            List<Integer> answers
    ) {

    }
}
