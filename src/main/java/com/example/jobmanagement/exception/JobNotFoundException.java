package com.example.jobmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested job cannot be found in the system.
 * This is a runtime exception that indicates a job lookup operation failed.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class JobNotFoundException extends RuntimeException {
    /**
     * Constructs a new JobNotFoundException with a detailed message.
     *
     * @param id the ID of the job that was not found
     */
    public JobNotFoundException(Long id) {
        super("Job not found with id: " + id);
    }
} 