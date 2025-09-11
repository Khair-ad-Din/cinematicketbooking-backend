package com.WisdomMonkey.CinemaTicketBooking_Backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
//                        .requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
//                        .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .csrf(csrf -> csrf.disable())  // Esto deshabilitará CSRF
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
}
