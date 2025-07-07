package org.withtime.be.withtimebe.domain.auth.service.command;

import java.time.Duration;

public interface TokenQueryService {
    Duration getAccessTokenExpiration();
    Duration getRefreshTokenExpiration();
}
