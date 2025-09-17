package com.WisdomMonkey.CinemaTicketBooking_Backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) Configuration for Movie Recommender API
 *
 * This configuration sets up API documentation using SpringDoc OpenAPI (Swagger 3).
 *
 * Access URLs (when running):
 * - Swagger UI: http://localhost:8080/api/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/api/v3/api-docs
 * - OpenAPI YAML: http://localhost:8080/api/v3/api-docs.yaml
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates the main OpenAPI configuration bean
     * This bean is automatically detected by SpringDoc and used to generate documentation
     *
     * @return OpenAPI instance with API metadata
     */
    @Bean
    public OpenAPI movieRecommenderOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        // API Title: Appears at the top of Swagger UI
                        .title("Movie Recommender API")

                        // API Version: Helps track API changes and compatibility
                        .version("v1.0.0")

                        // Contact Information: Who to contact for API support
                        .contact(new Contact()
                                .name("Movie Recommender Team")
                                // TODO: Add email and website when available
                                // .email("")
                                // .url("")
                        ));

        // TODO: Add more OpenAPI features as needed.
    }
}