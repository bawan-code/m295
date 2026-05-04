package ch.mahmud.bawan.job_marketplace.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jobPlatformOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Job Platform API")
                        .description("API documentation for the Job Platform backend")
                        .version("1.0.0"));
    }
}