package com.example.jobmanagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity class representing a job in the system.
 * This class maintains information about individual jobs and their assignments to technicians.
 * Jobs can be in one of three states: PENDING, IN_PROGRESS, or COMPLETED.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "job")
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

    /** Date and time when the job was completed. Null if not completed. */
    private LocalDateTime completedDate;

    /** Current status of the job */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Job status is required")
    private JobStatus status;

    /**
     * Enumeration representing the possible states of a job.
     * A job progresses through these states during its lifecycle.
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