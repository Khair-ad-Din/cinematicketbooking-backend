package com.WisdomMonkey.CinemaTicketBooking_Backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configures security settings for the app.
 * It defines which endpoints are public (no authentication required) and which
 * need authentication.
 *
 * Key Concepts:
 * - SecurityFilterChain: Defines the security rules for incoming HTTP requests
 * - CORS: Cross-Origin Resource Sharing (allows frontend apps to call our API)
 * - Request Matchers: Define URL patterns and their security requirements
 *
 * Development vs Production:
 * - Currently configured for development with permissive settings
 * - TODO: Implement proper JWT authentication in Phase 2
 */
@Configuration
@EnableWebSecurity // Enables Spring Security for this application
public class SecurityConfig {

    // Inject CORS configuration to allow cross-origin requests from frontend
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    /**
     * Main security configuration
     * This method tells Spring Security how to handle incoming requests
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                /*
                 * CSRF (Cross-Site Request Forgery) Protection:
                 * - Disabled for API development to allow easy testing with tools like Postman
                 * - In production, consider enabling CSRF protection for web applications
                 * - REST APIs typically don't need CSRF protection if using proper
                 * authentication
                 */
                .csrf(AbstractHttpConfigurer::disable)

                /*
                 * CORS (Cross-Origin Resource Sharing) Configuration:
                 * Uses the CORS configuration defined in CorsConfig.java
                 */
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                /*
                 * Authorization Rules:
                 * Define which URLs require authentication and which are public
                 * Order matters! More specific patterns should come first
                 */
                .authorizeHttpRequests(authz -> authz
                        /*
                         * Public Documentation Endpoints:
                         * Allow unrestricted access to API documentation
                         * - Swagger UI: Interactive API documentation interface
                         * - OpenAPI docs: JSON/YAML API specification
                         * - WebJars: Static resources for Swagger UI
                         */
                        .requestMatchers(
                                "/swagger-ui/**", // Swagger UI static files
                                "/swagger-ui.html", // Main Swagger UI page
                                "/v3/api-docs/**", // OpenAPI specification endpoints
                                "/v3/api-docs", // Main OpenAPI endpoint
                                "/swagger-resources/**", // Swagger configuration resources
                                "/webjars/**", // WebJar static resources (CSS, JS)
                                "/swagger-ui/index.html" // Swagger UI index page
                        ).permitAll()

                        /*
                         * Development Tools:
                         * H2 Console - In-memory database web interface
                         * WARNING: Remove this in production for security!
                         */
                        .requestMatchers("/h2-console/**").permitAll()

                        /*
                         * Monitoring Endpoints:
                         * Spring Boot Actuator provides application health and metrics
                         */
                        .requestMatchers("/actuator/health").permitAll()

                        /*
                         * Public Authentication Endpoints:
                         * These endpoints don't require authentication (users need them to get
                         * authenticated!)
                         * TODO: Implement these endpoints in Phase 2
                         */
                        .requestMatchers("/auth/register", "/auth/login").permitAll()

                        /*
                         * Development Setting - Allow All:
                         * Currently allowing all requests for development phase
                         * TODO Phase 2: Replace with proper JWT authentication:
                         * .anyRequest().authenticated()
                         */
                        .anyRequest().permitAll())

                /*
                 * Header Configuration:
                 * Configure security headers for the application
                 * sameOrigin: Allows framing only from the same origin (needed for H2 Console)
                 */
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        // Build and return the configured security filter chain
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}