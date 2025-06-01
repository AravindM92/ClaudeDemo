# Job Management System - Complete Implementation Guide

Create a Spring Boot application for managing technicians and their job assignments with the following specifications:

## Project Setup

1. Maven Configuration:
   ```xml
   <parent>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-parent</artifactId>
       <version>3.2.0</version>
   </parent>
   ```

2. Required Dependencies:
   - spring-boot-starter-web
   - spring-boot-starter-data-jpa
   - spring-boot-starter-validation
   - spring-boot-starter-security
   - postgresql (runtime)
   - lombok
   - springdoc-openapi-starter-webmvc-ui (version 2.3.0)
   - spring-boot-starter-test (test scope)
   - spring-security-test (test scope)

## Database Requirements

1. PostgreSQL Configuration:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/jobmanagement
       username: postgres
       password: postgres
       driver-class-name: org.postgresql.Driver
   ```

2. Entity Specifications:
   
   Technician Entity:
   ```java
   @Entity
   @Table(name = "technicians")
   - tech_id (Long, @Id, @GeneratedValue)
   - tech_name (String, @NotBlank)
   - dateOfJoining (LocalDate, @NotNull, @PastOrPresent)
   - jobs (List<Job>, @OneToMany, @JsonManagedReference)
   ```

   Job Entity:
   ```java
   @Entity
   @Table(name = "jobs")
   - job_id (Long, @Id, @GeneratedValue)
   - description (String, @NotBlank)
   - technician (Technician, @ManyToOne, @JsonBackReference)
   - createdDate (LocalDateTime, @NotNull)
   - completedDate (LocalDateTime)
   - status (JobStatus, @Enumerated(EnumType.STRING))
   ```

3. Job Status Enum:
   ```java
   public enum JobStatus {
       PENDING,
       IN_PROGRESS,
       COMPLETED
   }
   ```

## Application Structure

1. Package Organization:
   ```
   com.example.jobmanagement/
   ├── controller/
   │   ├── JobController.java
   │   └── TechnicianController.java
   ├── model/
   │   ├── Job.java
   │   ├── JobStatus.java
   │   └── Technician.java
   ├── repository/
   │   ├── JobRepository.java
   │   └── TechnicianRepository.java
   ├── service/
   │   ├── JobService.java
   │   └── TechnicianService.java
   ├── exception/
   │   ├── GlobalExceptionHandler.java
   │   ├── ResourceNotFoundException.java
   │   ├── JobNotFoundException.java
   │   ├── TechnicianNotFoundException.java
   │   └── InvalidJobStatusException.java
   ├── dto/
   │   └── ErrorResponse.java
   ├── config/
   │   └── SecurityConfig.java
   └── JobManagementApplication.java
   ```

## API Endpoints

1. Technician Controller (`/api/technicians`):
   ```
   POST /              - Create technician
   GET /               - Get all technicians
   GET /{id}           - Get technician by ID
   PUT /{id}           - Update technician
   DELETE /{id}        - Delete technician
   ```

2. Job Controller (`/api/jobs`):
   ```
   POST /              - Create job
   GET /               - Get all jobs
   GET /{id}           - Get job by ID
   GET /technician/{techId} - Get jobs by technician
   GET /status/{status} - Get jobs by status
   PUT /{id}           - Update job
   DELETE /{id}        - Delete job
   ```

## Exception Handling

1. Error Response Structure:
   ```java
   @Data @Builder
   public class ErrorResponse {
       private LocalDateTime timestamp;
       private int status;
       private String error;
       private String message;
       private String path;
       private Map<String, String> validationErrors;
   }
   ```

2. HTTP Status Codes:
   - 200: Successful operation
   - 201: Resource created
   - 204: No content (successful deletion)
   - 400: Bad request
   - 404: Resource not found
   - 409: Conflict
   - 422: Validation error
   - 500: Internal server error

## Security Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll());
        return http.build();
    }
}
```

## Application Properties

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    operationsSorter: method

server:
  port: 8080
  error:
    include-message: always
    include-binding-errors: always
```

## Testing Data

1. Sample Technician JSON:
   ```json
   {
     "techName": "John Doe",
     "dateOfJoining": "2024-01-01"
   }
   ```

2. Sample Job JSON:
   ```json
   {
     "description": "Fix printer",
     "technician": {
       "techId": 1
     },
     "status": "PENDING"
   }
   ```

## Postman Collection Structure

1. Technician Requests:
   - Create Technician (POST)
   - Get All Technicians (GET)
   - Get Technician by ID (GET)
   - Update Technician (PUT)
   - Delete Technician (DELETE)

2. Job Requests:
   - Create Job (POST)
   - Get All Jobs (GET)
   - Get Job by ID (GET)
   - Get Jobs by Technician (GET)
   - Get Jobs by Status (GET)
   - Update Job (PUT)
   - Delete Job (DELETE)

3. Error Scenarios:
   - Get Non-existent Technician
   - Create Job with Invalid Status
   - Create Job with Non-existent Technician
   - Update with Invalid Data

## Implementation Notes

1. Use constructor injection with @RequiredArgsConstructor
2. Implement proper bidirectional relationship handling
3. Include comprehensive API documentation
4. Add proper validation constraints
5. Implement proper error handling
6. Use appropriate logging

Please implement this system following all the specifications above. The implementation should be production-ready with proper error handling, input validation, and documentation. 