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
   ```xml
   <dependencies>
       <!-- Spring Boot Starters -->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-data-jpa</artifactId>
       </dependency>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-validation</artifactId>
       </dependency>
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-security</artifactId>
       </dependency>
       
       <!-- Database -->
       <dependency>
           <groupId>org.postgresql</groupId>
           <artifactId>postgresql</artifactId>
           <scope>runtime</scope>
       </dependency>
       
       <!-- Lombok -->
       <dependency>
           <groupId>org.projectlombok</groupId>
           <artifactId>lombok</artifactId>
           <version>1.18.30</version>
           <scope>provided</scope>
           <optional>true</optional>
       </dependency>
       
       <!-- OpenAPI/Swagger -->
       <dependency>
           <groupId>org.springdoc</groupId>
           <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
           <version>2.3.0</version>
       </dependency>
       
       <!-- Test Dependencies -->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-test</artifactId>
           <scope>test</scope>
       </dependency>
       <dependency>
           <groupId>org.springframework.security</groupId>
           <artifactId>spring-security-test</artifactId>
           <scope>test</scope>
       </dependency>
       <dependency>
           <groupId>org.mockito</groupId>
           <artifactId>mockito-core</artifactId>
           <version>5.10.0</version>
           <scope>test</scope>
       </dependency>
       <dependency>
           <groupId>com.h2database</groupId>
           <artifactId>h2</artifactId>
           <scope>test</scope>
       </dependency>
   </dependencies>
   ```

## Database Requirements

1. PostgreSQL Configuration (application.yml):
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/postgres
       username: postgres
       password: postgres
       driver-class-name: org.postgresql.Driver
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

2. Test Configuration (application-test.yml):
   ```yaml
   spring:
     datasource:
       url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
       username: sa
       password: 
       driver-class-name: org.h2.Driver
     jpa:
       hibernate:
         ddl-auto: create-drop
       show-sql: true
       properties:
         hibernate:
           format_sql: true
           dialect: org.hibernate.dialect.H2Dialect
     mvc:
       pathmatch:
         matching-strategy: ant_path_matcher

   springdoc:
     api-docs:
       enabled: false
     swagger-ui:
       enabled: false

   server:
     port: 0
     error:
       include-message: always
       include-binding-errors: always
   ```

3. Database Schema:
   ```sql
   -- Create technician table
   CREATE TABLE technician (
       tech_id SERIAL PRIMARY KEY,
       tech_name VARCHAR(100) NOT NULL,
       doj DATE NOT NULL
   );

   -- Create job table
   CREATE TABLE job (
       job_id SERIAL PRIMARY KEY,
       description TEXT NOT NULL,
       tech_id INTEGER NOT NULL,
       created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       completed_date TIMESTAMP,
       status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED')),
       CONSTRAINT fk_technician
           FOREIGN KEY (tech_id)
           REFERENCES technician(tech_id)
           ON DELETE RESTRICT
   );
   ```

## Entity Classes

1. Technician Entity:
   ```java
   /**
    * Entity class representing a technician in the job management system.
    * This class maintains the technician's basic information and their assigned jobs.
    */
   @Entity
   @Table(name = "technician")
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   @Builder
   @JsonIgnoreProperties({"hibernateLazyInitializer"})
   public class Technician {
       /** Unique identifier for the technician */
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long techId;

       /** Name of the technician */
       @NotBlank(message = "Technician name is required")
       private String techName;

       /** Date when the technician joined the organization */
       @NotNull(message = "Date of joining is required")
       @PastOrPresent(message = "Date of joining cannot be in the future")
       private LocalDate doj;

       /** List of jobs assigned to this technician */
       @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL)
       @JsonManagedReference
       private List<Job> jobs;
   }
   ```

2. Job Entity:
   ```java
   /**
    * Entity class representing a job in the system.
    * This class maintains information about individual jobs and their assignments to technicians.
    */
   @Entity
   @Table(name = "job")
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   @Builder
   public class Job {
       /** Unique identifier for the job */
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long jobId;

       /** Description of the job task */
       @NotBlank(message = "Job description is required")
       private String description;

       /** Technician assigned to this job */
       @ManyToOne(fetch = FetchType.LAZY)
       @JoinColumn(name = "tech_id")
       @JsonBackReference
       private Technician technician;

       /** Date and time when the job was created */
       @NotNull(message = "Created date is required")
       private LocalDateTime createdDate;

       /** Date and time when the job was completed */
       private LocalDateTime completedDate;

       /** Current status of the job */
       @Enumerated(EnumType.STRING)
       @NotNull(message = "Job status is required")
       private JobStatus status;

       /**
        * Enumeration representing the possible states of a job.
        */
       public enum JobStatus {
           /** Job is waiting to be started */
           PENDING,
           /** Job is currently being worked on */
           IN_PROGRESS,
           /** Job has been finished */
           COMPLETED
       }
   }
   ```

3. CreateTechnicianRequest DTO:
   ```java
   /**
    * Data Transfer Object for creating a new technician.
    * This class encapsulates the data required to create a new technician in the system.
    */
   @Data
   public class CreateTechnicianRequest {
       /** Name of the technician to be created */
       @NotBlank(message = "Technician name is required")
       private String techName;

       /** Date when the technician joined or will join */
       @NotNull(message = "Date of joining is required")
       @PastOrPresent(message = "Date of joining cannot be in the future")
       private LocalDate dateOfJoining;
   }
   ```

## Security Configuration

1. Main Security Config:
   ```java
   /**
    * Security configuration for the production environment.
    * This class configures Spring Security settings for the application.
    */
   @Configuration
   @EnableWebSecurity
   public class SecurityConfig {
       /**
        * Configures the security filter chain for the application.
        *
        * @param http the HttpSecurity object to configure
        * @return the configured SecurityFilterChain
        * @throws Exception if an error occurs during configuration
        */
       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http
               .csrf(csrf -> csrf.disable())
               .authorizeHttpRequests(auth -> auth
                   .anyRequest().permitAll()
               );
           return http.build();
       }
   }
   ```

2. Test Security Config:
   ```java
   /**
    * Security configuration for the test environment.
    * This class provides a simplified security configuration for testing purposes.
    */
   @TestConfiguration
   @EnableWebSecurity
   public class TestSecurityConfig {
       /**
        * Configures the security filter chain for testing.
        *
        * @param http the HttpSecurity object to configure
        * @return the configured SecurityFilterChain
        * @throws Exception if an error occurs during configuration
        */
       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
           http
               .csrf(csrf -> csrf.disable())
               .authorizeHttpRequests(auth -> auth
                   .anyRequest().permitAll()
               );
           return http.build();
       }
   }
   ```

## Test Classes

1. Controller Test Structure:
   ```java
   @WebMvcTest(XxxController.class)
   @Import(TestSecurityConfig.class)
   class XxxControllerTest {
       @Autowired
       private MockMvc mockMvc;

       @MockBean
       private XxxService xxxService;

       @Autowired
       private ObjectMapper objectMapper;

       @BeforeEach
       void setUp() {
           // Initialize test data
       }

       @Test
       void methodName_Scenario_ExpectedBehavior() throws Exception {
           // Arrange
           when(xxxService.method()).thenReturn(result);

           // Act & Assert
           mockMvc.perform(get("/api/xxx"))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$.field").value(value));
       }
   }
   ```

2. Service Test Structure:
   ```java
   @ExtendWith(MockitoExtension.class)
   class XxxServiceTest {
       @Mock
       private XxxRepository xxxRepository;

       @InjectMocks
       private XxxServiceImpl xxxService;

       @BeforeEach
       void setUp() {
           // Initialize test data
       }

       @Test
       void methodName_Scenario_ExpectedBehavior() {
           // Arrange
           when(xxxRepository.method()).thenReturn(result);

           // Act
           Type result = xxxService.method();

           // Assert
           assertNotNull(result);
           assertEquals(expected, result);
           verify(xxxRepository).method();
       }
   }
   ```

## Implementation Notes

1. Use constructor injection with @RequiredArgsConstructor
2. Implement proper bidirectional relationship handling
3. Include comprehensive API documentation using OpenAPI annotations
4. Add proper validation constraints
5. Implement proper error handling
6. Use appropriate logging
7. Write comprehensive unit tests for all components
8. Use H2 database for testing
9. Disable security for tests using TestSecurityConfig
10. Follow proper naming conventions for test methods (methodName_Scenario_ExpectedBehavior)

Please implement this system following all the specifications above. The implementation should be production-ready with proper error handling, input validation, documentation, and test coverage. 