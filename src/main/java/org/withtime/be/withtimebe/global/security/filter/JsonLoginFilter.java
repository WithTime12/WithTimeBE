package org.withtime.be.withtimebe.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.namul.api.payload.code.dto.supports.DefaultResponseErrorReasonDTO;
import org.namul.api.payload.writer.FailureResponseWriter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.withtime.be.withtimebe.domain.auth.dto.request.AuthRequestDTO;
import org.withtime.be.withtimebe.global.error.code.AuthErrorCode;

import java.io.IOException;

@Getter
@RequiredArgsConstructor
public class JsonLoginFilter extends OncePerRequestFilter {

    private static final RequestMatcher DEFAULT_REQUEST_MATCHER = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/api/v1/auth/login");
    private static final String USERNAME_PARAMETER = "username";
    private static final String PASSWORD_PARAMETER = "password";
    private final AuthenticationManager authenticationManager;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final FailureResponseWriter<DefaultResponseErrorReasonDTO> failureResponseWriter;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.requiresAuthentication(request)) {
            try {
                Authentication authentication = attemptAuthentication(request);

                if (authentication == null) {
                    return;
                }
                this.successfulAuthentication(request, response, authentication);
            } catch (Exception e) {
                handleException(response, e);
            }
        }
        else {
            filterChain.doFilter(request, response);
        }
    }

    public Authentication attemptAuthentication(HttpServletRequest request) throws AuthenticationException {
        try {
             AuthRequestDTO.Login requestBody = getBodyInRequest(request);

            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(requestBody.username(), requestBody.password());
            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (IOException e) {
            throw new AuthenticationServiceException("Json Parsing Error In Json Filter");
        } catch (Exception e) {
            throw new AuthenticationServiceException("CustomJsonUsernamePasswordLoginFilter(" + e.getClass() + "): " + e.getMessage());
        }
    }

    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        SecurityContext securityContext = securityContextHolderStrategy.createEmptyContext();
        securityContext.setAuthentication(authResult);
        securityContextRepository.saveContext(securityContext, request, response);
        securityContextHolderStrategy.setContext(securityContext);
        authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        String contentType = request.getContentType();
        return DEFAULT_REQUEST_MATCHER.matches(request) && contentType != null && contentType.equals(MediaType.APPLICATION_JSON_VALUE);
    }

    private AuthRequestDTO.Login getBodyInRequest(HttpServletRequest request) throws IOException{
        String content = new String((new HttpServletRequestWrapper(request)).getInputStream().readAllBytes());
        ObjectMapper om = new ObjectMapper();
        return om.readValue(content, AuthRequestDTO.Login.class);
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        ObjectMapper om = new ObjectMapper();
        DefaultResponseErrorReasonDTO reasonDTO = AuthErrorCode.FAIL_AUTH_LOGIN.getReason();
        response.setStatus(reasonDTO.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        om.writeValue(response.getOutputStream(), failureResponseWriter.onFailure(reasonDTO, e.getMessage()));
    }
}
