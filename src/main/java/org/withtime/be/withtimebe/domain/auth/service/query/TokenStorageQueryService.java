package org.withtime.be.withtimebe.domain.auth.service.query;

public interface TokenStorageQueryService {
    boolean isBlackList(String token);
    String getRefreshToken(Long id);
}
