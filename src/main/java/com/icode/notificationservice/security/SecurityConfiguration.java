package com.icode.notificationservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        final var publicPaths = new String[]{"/api/v1/email-confirmation"};

        return http
                .csrf()
                .disable()
                .cors(new CorsCustomizer())
//                .authenticationManager(reactiveAuthenticationManager())
//                .addFilterBefore(tokenGeneratorFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
//                .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .pathMatchers(publicPaths)
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .build();
    }

}
