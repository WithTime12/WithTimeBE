package org.withtime.be.withtimebe.domain.auth.service.command;

public interface TokenStorageCommandService {
    void addRefreshToken(Long id, String refresh);
    void addBlackList(String token);
    void deleteRefreshToken(Long userId);
}
