package com.xavelo.filocitas.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    private static final String FILOCITAS_API_SCOPE = "filocitas-api";
    /*
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/ping/**").permitAll()
                        .requestMatchers("/api/user/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/latency/**").permitAll()
                        .requestMatchers("/api/secure/**").hasAuthority(FILOCITAS_API_SCOPE)
                        .anyRequest().authenticated()
                        .anyRequest().permitAll()
                )
                //.oauth2ResourceServer(oauth2 -> oauth2.jwt());
        ;

        return http.build();
    } */
}
