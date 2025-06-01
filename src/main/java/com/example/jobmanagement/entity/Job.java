package com.example.jobmanagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @NotBlank(message = "Job description is required")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_id")
    @JsonBackReference
    private Technician technician;

    @NotNull(message = "Created date is required")
    private LocalDateTime createdDate;

    private LocalDateTime completedDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Job status is required")
    private JobStatus status;

    public enum JobStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }
} 