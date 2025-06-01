package com.example.jobmanagement.service;

import com.example.jobmanagement.entity.Job;

import java.util.List;

/**
 * Service interface for managing jobs in the system.
 * This interface defines the core business operations available for job management.
 */
public interface JobService {
    /**
     * Creates a new job in the system.
     *
     * @param job the job to create, must not be null
     * @return the created job with assigned ID and metadata
     * @throws TechnicianNotFoundException if the assigned technician doesn't exist
     * @throws IllegalArgumentException if the job data is invalid
     */
    Job createJob(Job job);

    /**
     * Retrieves a job by its ID.
     *
     * @param id the ID of the job to retrieve
     * @return the job with the specified ID
     * @throws JobNotFoundException if no job is found with the given ID
     */
    Job getJobById(Long id);

    /**
     * Retrieves all jobs in the system.
     *
     * @return a list of all jobs, empty list if no jobs exist
     */
    List<Job> getAllJobs();

    /**
     * Retrieves all jobs assigned to a specific technician.
     *
     * @param techId the ID of the technician
     * @return a list of jobs assigned to the technician, empty list if no jobs are assigned
     * @throws TechnicianNotFoundException if no technician is found with the given ID
     */
    List<Job> getJobsByTechnicianId(Long techId);

    /**
     * Retrieves all jobs with a specific status.
     *
     * @param status the status to filter by
     * @return a list of jobs with the specified status, empty list if no jobs match
     */
    List<Job> getJobsByStatus(Job.JobStatus status);

    /**
     * Updates an existing job.
     *
     * @param id the ID of the job to update
     * @param job the updated job data
     * @return the updated job
     * @throws JobNotFoundException if no job is found with the given ID
     * @throws TechnicianNotFoundException if the assigned technician doesn't exist
     * @throws IllegalArgumentException if the job data is invalid
     */
    Job updateJob(Long id, Job job);

    /**
     * Deletes a job from the system.
     *
     * @param id the ID of the job to delete
     * @throws JobNotFoundException if no job is found with the given ID
     */
    void deleteJob(Long id);
}