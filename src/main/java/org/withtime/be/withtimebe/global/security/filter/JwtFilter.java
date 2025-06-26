package org.withtime.be.withtimebe.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.service.MemberQueryService;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;
import org.withtime.be.withtimebe.global.util.JwtUtil;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_KEY = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final MemberQueryService memberQueryService;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if (token != null) {
            try {
                Long userId = jwtUtil.getUserId(token);
                Member member = memberQueryService.findById(userId);
                CustomUserDetails customUserDetails = new CustomUserDetails(member);

                Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(customUserDetails, "", customUserDetails.getAuthorities());
                this.successfulAuthentication(request, response, authentication);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                handleException(e);
            }
        }
        else {
            filterChain.doFilter(request, response);
        }
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_KEY);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        SecurityContext securityContext = securityContextHolderStrategy.createEmptyContext();
        securityContext.setAuthentication(authResult);
        securityContextRepository.saveContext(securityContext, request, response);
        securityContextHolderStrategy.setContext(securityContext);
    }

    private void handleException(Exception e) {
        // TODO: Handle Exception
    }
}
