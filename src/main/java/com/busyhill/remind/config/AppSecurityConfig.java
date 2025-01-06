package com.busyhill.remind.config;

import com.busyhill.remind.auth.RemindAuthEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class AppSecurityConfig {

    private final RemindAuthEntryPoint remindAuthEntryPoint;

    @Value("${remind.accepted.unauthenticated.routes:logout,login,reset-password}")
    private String acceptedRoutes;

    public AppSecurityConfig(RemindAuthEntryPoint remindAuthEntryPoint) {
        this.remindAuthEntryPoint = remindAuthEntryPoint;
    }

    @Bean
    public SecurityFilterChain appSecurity(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // defence
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(acceptedRoutes.split(","))
                        .permitAll()
                        .anyRequest().authenticated()
                ) // authentication
                .exceptionHandling(error -> error.authenticationEntryPoint(remindAuthEntryPoint)) // authentication
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // infrastructure
                .build();
    }
}
