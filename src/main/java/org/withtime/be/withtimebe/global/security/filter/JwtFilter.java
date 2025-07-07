package org.withtime.be.withtimebe.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.code.DefaultResponseErrorCode;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.namul.api.payload.error.exception.ServerApplicationException;
import org.namul.api.payload.writer.FailureResponseWriter;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.withtime.be.withtimebe.domain.member.entity.Member;
import org.withtime.be.withtimebe.domain.member.service.MemberQueryService;
import org.withtime.be.withtimebe.global.security.constants.AuthenticationConstants;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;
import org.withtime.be.withtimebe.global.util.CookieUtil;
import org.withtime.be.withtimebe.global.util.JwtUtil;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberQueryService memberQueryService;
    private final FailureResponseWriter<DefaultResponseErrorReasonDTO> failureResponseWriter;
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
            } catch (ServerApplicationException e) {
                handleServerApplicationException(response, e);
            } catch (Exception e) {
                handleException(response, e);
            }
        }
        else {
            filterChain.doFilter(request, response);
        }
    }

    private String getToken(HttpServletRequest request) {
        return CookieUtil.getCookie(request, AuthenticationConstants.ACCESS_TOKEN_NAME);
    }

    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        SecurityContext securityContext = securityContextHolderStrategy.createEmptyContext();
        securityContext.setAuthentication(authResult);
        securityContextRepository.saveContext(securityContext, request, response);
        securityContextHolderStrategy.setContext(securityContext);
    }

    private void handleServerApplicationException(HttpServletResponse response, ServerApplicationException e) throws IOException {
        ObjectMapper om = new ObjectMapper();
        DefaultResponseErrorReasonDTO reasonDTO = (DefaultResponseErrorReasonDTO) e.getCode().getReason();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(reasonDTO.getHttpStatus().value());
        om.writeValue(response.getOutputStream(), failureResponseWriter.onFailure(reasonDTO, null));
    }

    private void handleException(HttpServletResponse response,  Exception e) throws IOException {
        ObjectMapper om = new ObjectMapper();
        DefaultResponseErrorReasonDTO reasonDTO = DefaultResponseErrorCode._UNAUTHORIZED.getReason();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(reasonDTO.getHttpStatus().value());
        om.writeValue(response.getOutputStream(), failureResponseWriter.onFailure(reasonDTO, e.getMessage()));
    }
}
