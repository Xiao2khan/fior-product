package com.fiordelisi.fiordelisiproduct.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class I18nConfig implements WebMvcConfigurer {

    private final PathLocaleInterceptor pathLocaleInterceptor;
    private final AdminRedirectInterceptor adminRedirectInterceptor;

    public I18nConfig(PathLocaleInterceptor pathLocaleInterceptor, AdminRedirectInterceptor adminRedirectInterceptor) {
        this.pathLocaleInterceptor = pathLocaleInterceptor;
        this.adminRedirectInterceptor = adminRedirectInterceptor;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminRedirectInterceptor);
        registry.addInterceptor(pathLocaleInterceptor);
    }
}


