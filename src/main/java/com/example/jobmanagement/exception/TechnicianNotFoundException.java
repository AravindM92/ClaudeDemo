package com.example.jobmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested technician cannot be found in the system.
 * This is a runtime exception that indicates a technician lookup operation failed.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TechnicianNotFoundException extends RuntimeException {
    /**
     * Constructs a new TechnicianNotFoundException with a detailed message.
     *
     * @param id the ID of the technician that was not found
     */
    public TechnicianNotFoundException(Long id) {
        super("Technician not found with id: " + id);
    }
} 