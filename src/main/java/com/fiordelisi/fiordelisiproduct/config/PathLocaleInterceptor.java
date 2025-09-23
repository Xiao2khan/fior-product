package com.fiordelisi.fiordelisiproduct.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

@Component
public class PathLocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        // Expecting paths like "/en/..." or "/vi/..."
        if (uri != null && uri.length() >= 3 && uri.charAt(0) == '/') {
            int nextSlash = uri.indexOf('/', 1);
            String firstSegment = nextSlash > 0 ? uri.substring(1, nextSlash) : uri.substring(1);
            Locale locale = null;
            if ("en".equalsIgnoreCase(firstSegment)) {
                locale = Locale.ENGLISH;
            } else if ("vi".equalsIgnoreCase(firstSegment)) {
                locale = new Locale("vi");
            }
            if (locale != null) {
                LocaleResolver resolver = RequestContextUtils.getLocaleResolver(request);
                if (resolver != null) {
                    resolver.setLocale(request, response, locale);
                }
            }
        }
        return true;
    }
}


