package com.example.jobmanagement.controller;

import com.example.jobmanagement.entity.Job;
import com.example.jobmanagement.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing jobs in the system.
 * This controller provides the API endpoints for job-related operations.
 * All endpoints are under the "/api/jobs" base path.
 */
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Job Management", description = "APIs for managing jobs")
public class JobController {
    private final JobService jobService;

    /**
     * Retrieves all jobs in the system.
     *
     * @return ResponseEntity containing a list of all jobs
     * @response 200 List of jobs retrieved successfully
     */
    @GetMapping
    @Operation(summary = "Get all jobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    /**
     * Retrieves a specific job by ID.
     *
     * @param id the ID of the job to retrieve
     * @return ResponseEntity containing the requested job
     * @response 200 Job found and returned successfully
     * @response 404 Job not found with the given ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a job by ID")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    /**
     * Retrieves all jobs assigned to a specific technician.
     *
     * @param techId the ID of the technician
     * @return ResponseEntity containing a list of jobs assigned to the technician
     * @response 200 Jobs retrieved successfully
     * @response 404 Technician not found with the given ID
     */
    @GetMapping("/technician/{techId}")
    @Operation(summary = "Get jobs by technician ID")
    public ResponseEntity<List<Job>> getJobsByTechnicianId(@PathVariable Long techId) {
        return ResponseEntity.ok(jobService.getJobsByTechnicianId(techId));
    }

    /**
     * Retrieves all jobs with a specific status.
     *
     * @param status the status to filter by
     * @return ResponseEntity containing a list of jobs with the specified status
     * @response 200 Jobs retrieved successfully
     * @response 400 Invalid job status provided
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get jobs by status")
    public ResponseEntity<List<Job>> getJobsByStatus(@PathVariable Job.JobStatus status) {
        return ResponseEntity.ok(jobService.getJobsByStatus(status));
    }

    /**
     * Creates a new job in the system.
     *
     * @param job the job to create
     * @return ResponseEntity containing the created job
     * @response 201 Job created successfully
     * @response 400 Invalid job data provided
     * @response 404 Referenced technician not found
     */
    @PostMapping
    @Operation(summary = "Create a new job")
    public ResponseEntity<Job> createJob(@Valid @RequestBody Job job) {
        return new ResponseEntity<>(jobService.createJob(job), HttpStatus.CREATED);
    }

    /**
     * Updates an existing job.
     *
     * @param id the ID of the job to update
     * @param job the updated job data
     * @return ResponseEntity containing the updated job
     * @response 200 Job updated successfully
     * @response 400 Invalid job data provided
     * @response 404 Job not found with the given ID
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a job")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @Valid @RequestBody Job job) {
        return ResponseEntity.ok(jobService.updateJob(id, job));
    }

    /**
     * Deletes a job from the system.
     *
     * @param id the ID of the job to delete
     * @return ResponseEntity with no content
     * @response 204 Job deleted successfully
     * @response 404 Job not found with the given ID
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a job")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
} 