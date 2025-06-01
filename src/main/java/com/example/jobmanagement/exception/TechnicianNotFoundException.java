package com.example.jobmanagement.exception;

public class TechnicianNotFoundException extends ResourceNotFoundException {
    public TechnicianNotFoundException(Long id) {
        super(String.format("Technician not found with id: %d", id));
    }
} 