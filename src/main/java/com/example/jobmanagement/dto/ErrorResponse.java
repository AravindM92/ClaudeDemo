package com.example.jobmanagement.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data Transfer Object for error responses.
 * Used to provide structured error information in API responses.
 */
@Data
@Builder
public class ErrorResponse {
    /** Timestamp when the error occurred */
    private LocalDateTime timestamp;
    
    /** HTTP status code */
    private int status;
    
    /** Error type/category */
    private String error;
    
    /** Detailed error message */
    private String message;
    
    /** Request path that caused the error */
    private String path;
    
    /** Map of field-specific validation errors */
    private Map<String, String> validationErrors;
} 