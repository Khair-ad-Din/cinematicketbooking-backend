package com.WisdomMonkey.CinemaTicketBooking_Backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * CORS (Cross-Origin Resource Sharing) Configuration
 * Without CORS config, browser would block the frontend from calling backend API.
 * - Enables frontend applications to communicate with API
 * - Provides control to which domains can access API
 */
@Configuration
public class CorsConfig {

    /**
     * Creates and configures CORS settings for the entire application
     * This bean is automatically used by Spring Security (see SecurityConfig.java)
     *
     * @return CorsConfigurationSource with our CORS rules
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Create a new CORS configuration
        CorsConfiguration configuration = new CorsConfiguration();

        /*
         * Allowed Origins: Which domains can make requests to our API
         * - TODO Production: Add production domain
         *
         * Security Note: Never use "*" for allowedOrigins in production
         * when allowCredentials is true!
         */
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",  // Angular development server
                "http://localhost:3000"   // React/Next.js development server
        ));

        /*
         * Allowed HTTP Methods: Which HTTPs can be used
         */
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        /*
         * Allowed Headers: Which headers can be sent in requests
         * "*" all headers - for dev
         */
        configuration.addAllowedHeader("*");

        /*
         * Allow Credentials: Essential for authentication
         * - Enables sending cookies, authorization headers, and TLS client certificates
         * - Required for JWT tokens in Authorization header
         * - Required for session-based authentication
         *
         * Security Warning: When true, allowedOrigins cannot be "*"
         */
        configuration.setAllowCredentials(true);

        /*
         * Exposed Headers: Which response headers the frontend can access
         * By default, browsers only expose basic headers like Content-Type
         * We need to explicitly expose custom headers:
         * - Authorization: For JWT token responses
         * - X-Total-Count: For pagination information
         * - Content-Type: Standard header (included for completeness)
         */
        configuration.addExposedHeader("Authorization");  // JWT tokens
        configuration.addExposedHeader("Content-Type");   // Response content type
        configuration.addExposedHeader("X-Total-Count");  // Pagination support

        /*
         * Apply Configuration to All Endpoints:
         * "/**" means these CORS rules apply to all URL patterns
         * You could be more specific if needed:
         * - "/api/**" for API endpoints only
         * - "/public/**" for public endpoints only
         */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}