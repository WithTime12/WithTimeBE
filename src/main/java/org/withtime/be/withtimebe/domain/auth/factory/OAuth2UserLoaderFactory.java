package org.withtime.be.withtimebe.domain.auth.factory;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OAuth2UserLoaderFactory {

    private final Map<String, OAuth2UserLoader> oAuth2UserLoaderMap = new ConcurrentHashMap<>();

    public OAuth2UserLoaderFactory(List<OAuth2UserLoader> oAuth2UserLoaders) {
        oAuth2UserLoaders.forEach(
                oAuth2UserLoader -> oAuth2UserLoaderMap.put(oAuth2UserLoader.getSocialType().toLowerCase(), oAuth2UserLoader)
        );
    }

    public OAuth2UserLoader getUserLoader(String provider) {
        return oAuth2UserLoaderMap.get(provider.toLowerCase());
    }
}
