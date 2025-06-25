package org.withtime.be.withtimebe.global.security;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.withtime.be.withtimebe.domain.member.service.MemberQueryService;
import org.withtime.be.withtimebe.global.security.filter.JsonLoginFilter;
import org.withtime.be.withtimebe.global.security.filter.JwtFilter;
import org.withtime.be.withtimebe.global.util.JwtUtil;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String API_PREFIX = "/api/v1";
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final MemberQueryService memberQueryService;
    private final JwtUtil jwtUtil;

    private String[] allowUrl = {
            API_PREFIX + "/auth/**"
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(allowUrl).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jsonLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter(), JsonLoginFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
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
        return new JsonLoginFilter(authenticationManager, authenticationSuccessHandler, requestSecurityContextRepository());
    }

    @Bean
    Filter jwtFilter() {
        return new JwtFilter(jwtUtil, memberQueryService, requestSecurityContextRepository());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
