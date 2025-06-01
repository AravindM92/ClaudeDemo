package com.example.jobmanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Job Management System.
 * This Spring Boot application provides REST APIs for managing technicians and their job assignments.
 * The application uses Spring Security, JPA, and PostgreSQL for data persistence.
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Job Management System API",
        version = "1.0",
        description = "REST API for managing technicians and their job assignments"
    )
)
public class JobManagementApplication {
    /**
     * Main method that starts the Spring Boot application.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(JobManagementApplication.class, args);
    }
} 