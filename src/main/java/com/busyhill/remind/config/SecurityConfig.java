package com.busyhill.remind.config;

import com.busyhill.remind.auth.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // we need authentication and authorization
        http.csrf(Customizer.withDefaults())
                .oauth2Client(httpSecurityOAuth2ClientConfigurer -> {})
                .logout(Customizer.withDefaults())
                .exceptionHandling(Customizer.withDefaults())
                .authorizeHttpRequests(AbstractRequestMatcherRegistry::anyRequest);
                return http.build();
    }

}
