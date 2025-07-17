package org.withtime.be.withtimebe.global.security;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.namul.api.payload.writer.FailureResponseWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.withtime.be.withtimebe.domain.auth.service.query.TokenStorageQueryService;
import org.withtime.be.withtimebe.domain.member.service.query.MemberQueryService;
import org.withtime.be.withtimebe.global.security.filter.JsonLoginFilter;
import org.withtime.be.withtimebe.global.security.filter.JwtFilter;
import org.withtime.be.withtimebe.global.security.handler.CustomAccessDeniedHandler;
import org.withtime.be.withtimebe.global.security.handler.CustomAuthenticationEntryPoint;
import org.withtime.be.withtimebe.global.util.JwtUtil;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private static final String API_PREFIX = "/api/v1";
    private final TokenStorageQueryService tokenStorageQueryService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final MemberQueryService memberQueryService;
    private final JwtUtil jwtUtil;
    private final FailureResponseWriter<DefaultResponseErrorReasonDTO> failureResponseWriter;

    private String[] allowUrl = {
            API_PREFIX + "/auth/**",
            API_PREFIX + "/notices/**",
            API_PREFIX + "/faqs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
    };

    private RequestMatcher[] admin = {
            requestMatcher(HttpMethod.GET, API_PREFIX + "/notices/trash"),
            requestMatcher(HttpMethod.POST, API_PREFIX + "/notices/**"),
            requestMatcher(HttpMethod.PUT, API_PREFIX + "/notices/**"),
            requestMatcher(HttpMethod.PATCH, API_PREFIX + "/notices/**"),
            requestMatcher(HttpMethod.DELETE, API_PREFIX + "/notices/**"),

            requestMatcher(HttpMethod.POST, API_PREFIX + "/faqs/**"),
            requestMatcher(HttpMethod.PUT, API_PREFIX + "/faqs/**"),
            requestMatcher(HttpMethod.DELETE, API_PREFIX + "/faqs/**"),

            requestMatcher(HttpMethod.POST, API_PREFIX + "/regions/codes"),
            requestMatcher(HttpMethod.POST, API_PREFIX + "/regions"),
            requestMatcher(HttpMethod.POST, API_PREFIX + "/regions/bundle"),
            requestMatcher(HttpMethod.GET, API_PREFIX + "/regions/codes"),
            requestMatcher(HttpMethod.DELETE, API_PREFIX + "/regions/codes/**"),
            requestMatcher(HttpMethod.DELETE, API_PREFIX + "/regions/**"),

            requestMatcher(HttpMethod.POST, API_PREFIX + "/weather/trigger"),

            requestMatcher(HttpMethod.GET, API_PREFIX + "/members/membership"),
            requestMatcher(HttpMethod.PUT, API_PREFIX + "/members/*/membership"),
            requestMatcher(HttpMethod.GET, API_PREFIX + "/visit-logs/**"),

            requestMatcher(HttpMethod.GET, API_PREFIX + "/dateplaces/management"),
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(admin).hasRole("ADMIN")
                        .requestMatchers(allowUrl).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jsonLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), JsonLoginFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(authenticationEntryPoint())
                )
                .cors( cors -> cors.configurationSource(corsConfigurationSource()))
        ;
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityContextRepository requestSecurityContextRepository() {
        return new RequestAttributeSecurityContextRepository();
    }


    @Bean
    Filter jsonLoginFilter(AuthenticationManager authenticationManager) {
        return new JsonLoginFilter(authenticationManager, authenticationSuccessHandler,failureResponseWriter, requestSecurityContextRepository());
    }

    @Bean
    Filter jwtFilter() {
        return new JwtFilter(jwtUtil, memberQueryService, tokenStorageQueryService, failureResponseWriter, requestSecurityContextRepository());
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint(failureResponseWriter);
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(failureResponseWriter);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("http://localhost:5173"); // 실배포 주소 나중에 추가
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private RequestMatcher requestMatcher(HttpMethod method, String url) {
        return PathPatternRequestMatcher.withDefaults().matcher(method, url);
    }
}
