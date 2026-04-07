package com.hospital.config;

import com.hospital.service.TokenStore;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final TokenStore tokenStore;

    public AuthFilter(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    private static final String ROOT_PATH = "/";

    private static final List<String> PUBLIC_PATH_PREFIXES = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/departments/list",
            "/api/doctors/by-department",
            "/api/stats/summary",
            "/index.html",
            "/css/",
            "/js/",
            "/pages/"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("X-Auth-Token");
        if (token == null || token.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Missing or invalid token\"}");
            return;
        }

        TokenStore.TokenInfo info = tokenStore.getTokenInfo(token);
        if (info == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid or expired token\"}");
            return;
        }

        if (path.startsWith("/api/admin/") && !"ADMIN".equals(info.role())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Admin access required\"}");
            return;
        }
        if (path.startsWith("/api/doctor/") && !"DOCTOR".equals(info.role())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Doctor access required\"}");
            return;
        }
        if (path.startsWith("/api/patient/") && !"PATIENT".equals(info.role())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Patient access required\"}");
            return;
        }

        request.setAttribute("userId", info.userId());
        request.setAttribute("userRole", info.role());
        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        if (ROOT_PATH.equals(path)) {
            return true;
        }
        return PUBLIC_PATH_PREFIXES.stream().anyMatch(path::startsWith);
    }
}
