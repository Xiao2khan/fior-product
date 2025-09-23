package com.fiordelisi.fiordelisiproduct.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminRedirectInterceptor implements HandlerInterceptor {

    private static final String[] STATIC_PREFIXES = new String[]{
            "/css/", "/js/", "/image/", "/webjars/"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // Skip static resources
        for (String prefix : STATIC_PREFIXES) {
            if (uri.startsWith(prefix)) {
                return true;
            }
        }

        String lower = uri.toLowerCase();
        if (lower.contains("admin")) {
            boolean isAllowedAdmin = lower.startsWith("/admin") || lower.startsWith("/admin-login") || lower.startsWith("/admin-logout");
            if (!isAllowedAdmin) {
                response.sendRedirect("/en");
                return false;
            }
        }
        return true;
    }
}


