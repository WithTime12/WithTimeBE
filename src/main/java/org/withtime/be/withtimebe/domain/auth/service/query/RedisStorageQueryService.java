package org.withtime.be.withtimebe.domain.auth.service.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.withtime.be.withtimebe.domain.auth.constants.TokenStorageConstants;
import org.withtime.be.withtimebe.global.util.RedisUtil;

@Service
@RequiredArgsConstructor
public class RedisStorageQueryService implements TokenStorageQueryService {

    private final RedisUtil redisUtil;

    @Override
    public boolean isBlackList(String token) {
        return Boolean.TRUE.equals(redisUtil.has(TokenStorageConstants.BLACKLIST_PREFIX + token));
    }

    @Override
    public String getRefreshToken(Long id) {
        return redisUtil.get(TokenStorageConstants.REFRESH_TOKEN_PREFIX + id, String.class);
    }
}
