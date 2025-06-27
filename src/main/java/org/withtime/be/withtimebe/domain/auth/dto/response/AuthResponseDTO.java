package org.withtime.be.withtimebe.domain.auth.dto.response;

import lombok.Builder;

public record AuthResponseDTO() {

    @Builder
    public record Login (
        String accessToken,
        String refreshToken
    ) {

    }
}
