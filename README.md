# Job Management System

A Spring Boot application for managing technicians and their assigned jobs. This system provides REST APIs for creating, reading, updating, and deleting technicians and jobs.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Project Setup](#project-setup)
- [Database Setup](#database-setup)
- [Application Configuration](#application-configuration)
- [Entity Classes](#entity-classes)
- [API Documentation](#api-documentation)
- [Testing with Postman](#testing-with-postman)
- [Running the Application](#running-the-application)

## Prerequisites
- Java 17 or higher
- PostgreSQL 12 or higher
- Maven (or use the included Maven wrapper)
- Postman (for testing APIs)

## Project Setup

### 1. Create Maven Project
Create a new Maven project with the following `pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.3</version>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>job-management</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <java.version>17</java.version>
    </properties>

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
            <optional>true</optional>
        </dependency>

        <!-- OpenAPI/Swagger -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.3.0</version>
        </dependency>

        <!-- Dev Tools -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### 2. Create Maven Wrapper
Run the following command in your project directory:
```bash
mvn wrapper:wrapper
```

## Database Setup

### 1. Create Database Schema
Create a file named `database_setup.sql`:

```sql
-- Drop tables if they exist
DROP TABLE IF EXISTS job;
DROP TABLE IF EXISTS technician;

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

### 2. Create Sample Data
Create a file named `data.sql` in `src/main/resources`:

```sql
-- Insert technicians first
INSERT INTO technician (tech_id, tech_name, doj) VALUES
    (1, 'John Doe', '2024-01-15'),
    (2, 'Jane Smith', '2024-02-01')
ON CONFLICT (tech_id) DO NOTHING;

-- Then insert jobs
INSERT INTO job (description, tech_id, created_date, completed_date, status) 
SELECT 'Fix server connectivity issue', 1, '2024-03-10 09:00:00', '2024-03-10 14:30:00', 'COMPLETED'
WHERE NOT EXISTS (
    SELECT 1 FROM job WHERE description = 'Fix server connectivity issue'
);

INSERT INTO job (description, tech_id, created_date, completed_date, status)
SELECT 'Install new software', 1, '2024-03-11 10:00:00', NULL, 'IN_PROGRESS'
WHERE NOT EXISTS (
    SELECT 1 FROM job WHERE description = 'Install new software'
);

INSERT INTO job (description, tech_id, created_date, completed_date, status)
SELECT 'Setup network printer', 2, '2024-03-11 11:00:00', NULL, 'PENDING'
WHERE NOT EXISTS (
    SELECT 1 FROM job WHERE description = 'Setup network printer'
);
```

## Application Configuration

### 1. Application Properties
Create `application.yaml` in `src/main/resources`:

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
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
    defer-datasource-initialization: true
  
  sql:
    init:
      mode: always
      continue-on-error: true

server:
  port: 8080

logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

## Entity Classes

### 1. Technician Entity
Create `Technician.java`:

```java
package com.example.jobmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "technician")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Technician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_id")
    private Long techId;

    @Column(name = "tech_name", nullable = false)
    private String techName;

    @Column(name = "doj", nullable = false)
    private LocalDate doj;

    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties("technician")
    private List<Job> jobs;
}
```

### 2. Job Entity
Create `Job.java`:

```java
package com.example.jobmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_id", nullable = false)
    @ToString.Exclude
    @JsonIgnoreProperties({"jobs", "hibernateLazyInitializer"})
    private Technician technician;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    public enum JobStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }
}
```

## API Documentation

The application uses Springdoc OpenAPI for API documentation. Access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Testing with Postman

Import the provided Postman collection file: `Job_Management_API.postman_collection.json`

The collection includes all API endpoints with example requests and responses:

1. Technicians API:
   - Create Technician (POST /api/technicians)
   - Get All Technicians (GET /api/technicians)
   - Get Technician by ID (GET /api/technicians/{id})
   - Update Technician (PUT /api/technicians/{id})
   - Delete Technician (DELETE /api/technicians/{id})

2. Jobs API:
   - Create Job (POST /api/jobs)
   - Get All Jobs (GET /api/jobs)
   - Get Job by ID (GET /api/jobs/{id})
   - Get Jobs by Technician (GET /api/jobs/technician/{techId})
   - Get Jobs by Status (GET /api/jobs/status/{status})
   - Update Job (PUT /api/jobs/{id})
   - Delete Job (DELETE /api/jobs/{id})

## Running the Application

1. Start PostgreSQL server
2. Create the database tables using `database_setup.sql`
3. Run the application using Maven wrapper:
```bash
./mvnw spring-boot:run  # For Unix/Linux/Mac
.\mvnw.cmd spring-boot:run  # For Windows
```

The application will start on `http://localhost:8080`

## Common Issues and Solutions

1. If Maven is not found in PATH:
   - Use the Maven wrapper (mvnw or mvnw.cmd)

2. Database connection issues:
   - Verify PostgreSQL is running
   - Check database credentials in application.yaml
   - Ensure database tables are created

3. Infinite recursion in JSON:
   - Fixed using @JsonIgnoreProperties annotations
   - Proper handling of bidirectional relationships 