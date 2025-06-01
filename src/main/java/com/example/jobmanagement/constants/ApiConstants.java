package com.example.jobmanagement.constants;

public final class ApiConstants {
    private ApiConstants() {}

    // API Endpoints
    public static final String API_BASE_PATH = "/api";
    public static final String TECHNICIANS_PATH = API_BASE_PATH + "/technicians";
    public static final String JOBS_PATH = API_BASE_PATH + "/jobs";

    // Error Messages
    public static final String TECHNICIAN_NOT_FOUND = "Technician not found with id: %d";
    public static final String JOB_NOT_FOUND = "Job not found with id: %d";

    // Status Messages
    public static final String TECHNICIAN_CREATED = "Technician created successfully";
    public static final String JOB_CREATED = "Job created successfully";
    public static final String TECHNICIAN_UPDATED = "Technician updated successfully";
    public static final String JOB_UPDATED = "Job updated successfully";
    public static final String TECHNICIAN_DELETED = "Technician deleted successfully";
    public static final String JOB_DELETED = "Job deleted successfully";
} 