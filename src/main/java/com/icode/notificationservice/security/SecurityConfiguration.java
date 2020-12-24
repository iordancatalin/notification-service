package com.icode.notificationservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        final var publicPaths = new String[]{
                "/api/v1/email-confirmation",
                "/api/v1/reset-password",
                "/api/v1/notification/**",
                "/api/v1/notifications/*"
        };

        return http
                .csrf()
                .disable()
                .cors(new CorsCustomizer())
                .authorizeExchange()
                .pathMatchers(publicPaths)
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .build();
    }

}
