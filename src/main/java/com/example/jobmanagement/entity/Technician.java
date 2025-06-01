package com.example.jobmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a technician in the job management system.
 * This class maintains the technician's basic information and their assigned jobs.
 * Each technician can have multiple jobs assigned to them at any given time.
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

    /** List of jobs assigned to this technician. Managed bidirectionally. */
    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Job> jobs = new ArrayList<>();
} 