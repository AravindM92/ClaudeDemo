package com.example.jobmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an invalid job status is provided.
 * This exception indicates that the requested job status transition is not allowed.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidJobStatusException extends RuntimeException {
    /**
     * Constructs a new InvalidJobStatusException with a detailed message.
     *
     * @param message description of the invalid status condition
     */
    public InvalidJobStatusException(String message) {
        super(message);
    }
} 