package com.ecommerce.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF as this is a stateless API, typically protected by gateway
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/v1/orders/**").permitAll() // Adjust as per actual auth requirements, gateway handles primary auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Allow access to OpenAPI docs
                .anyRequest().authenticated() // Secure other endpoints if any; or permitAll() if gateway is sole authenticator
            );
        // If you extract JWT details passed by gateway, you might add a custom filter here
        return http.build();
    }
}
