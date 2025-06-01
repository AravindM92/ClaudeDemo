package com.example.jobmanagement.config;

import com.example.jobmanagement.dto.CreateTechnicianRequest;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private int serverPort;

    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server");

        Contact contact = new Contact()
                .name("Job Management Team")
                .email("support@jobmanagement.com")
                .url("https://www.jobmanagement.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Job Management System API")
                .version("1.0.0")
                .description("REST API documentation for Job Management System")
                .contact(contact)
                .license(license);

        Components components = new Components()
                .addSchemas("CreateTechnicianRequest", new Schema<CreateTechnicianRequest>()
                        .type("object")
                        .description("Request object for creating a new technician")
                        .addProperty("techName", new Schema<String>()
                                .type("string")
                                .description("Name of the technician")
                                .example("John Doe"))
                        .addProperty("dateOfJoining", new Schema<String>()
                                .type("string")
                                .format("date")
                                .description("Date when the technician joined")
                                .example("2024-01-01")));

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer))
                .components(components);
    }
} 