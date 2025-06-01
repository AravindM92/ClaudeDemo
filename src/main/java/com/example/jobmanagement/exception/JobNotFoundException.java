package com.example.jobmanagement.exception;

public class JobNotFoundException extends ResourceNotFoundException {
    public JobNotFoundException(Long id) {
        super(String.format("Job not found with id: %d", id));
    }
} 