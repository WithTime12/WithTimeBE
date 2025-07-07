package org.withtime.be.withtimebe.domain.auth.service.query;

import java.time.Duration;

public interface TokenQueryService {
    Duration getAccessTokenExpiration();
    Duration getRefreshTokenExpiration();
}
