package org.withtime.be.withtimebe.global.security.manager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.withtime.be.withtimebe.global.security.domain.CustomUserDetails;

public interface TokenManager {
    void addToken(HttpServletRequest request, HttpServletResponse response,CustomUserDetails customUserDetails);
}
