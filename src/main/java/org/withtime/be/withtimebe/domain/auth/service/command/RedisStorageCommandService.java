package org.withtime.be.withtimebe.domain.auth.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.auth.constants.TokenStorageConstants;
import org.withtime.be.withtimebe.domain.auth.service.query.TokenQueryService;
import org.withtime.be.withtimebe.global.util.RedisUtil;


@Service
@RequiredArgsConstructor
public class RedisStorageCommandService implements TokenStorageCommandService {

    private final RedisUtil redisUtil;
    private final TokenQueryService tokenQueryService;

    @Override
    public void addRefreshToken(Long id, String refresh) {
        redisUtil.set(TokenStorageConstants.REFRESH_TOKEN_PREFIX + id, refresh, tokenQueryService.getRefreshTokenExpiration());
    }

    @Override
    public void addBlackList(String token) {
        redisUtil.set(TokenStorageConstants.BLACKLIST_PREFIX + token, true, tokenQueryService.getRefreshTokenExpiration());
    }

    @Override
    public void deleteRefreshToken(Long userId) {
        redisUtil.delete(TokenStorageConstants.REFRESH_TOKEN_PREFIX + userId);
    }

}
