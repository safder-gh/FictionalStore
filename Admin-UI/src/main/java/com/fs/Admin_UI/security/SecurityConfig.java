package com.fs.Admin_UI.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable Spring's default form login and HTTP Basic auth
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // Authorize requests
                .authorizeHttpRequests(auth -> auth
                        // Permit Vaadin login route and static resources
                        .requestMatchers(
                                "/login",
                                "/frontend/**",
                                "/VAADIN/**",
                                "/icons/**",
                                "/images/**",
                                "/manifest.webmanifest",
                                "/sw.js",
                                "/offline.html"
                        ).permitAll()

                        // Protect REST API endpoints
                        .requestMatchers("/api/**").authenticated()

                        // Allow everything else for now
                        .anyRequest().permitAll()
                )

                // Disable CSRF for now (you can re-enable later with proper setup)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
