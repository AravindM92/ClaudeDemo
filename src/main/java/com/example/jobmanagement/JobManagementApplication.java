package com.example.jobmanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Job Management System API",
        version = "1.0",
        description = "REST API for managing technicians and their job assignments"
    )
)
public class JobManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobManagementApplication.class, args);
    }
} 