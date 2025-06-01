package com.example.jobmanagement.controller;

import com.example.jobmanagement.dto.CreateTechnicianRequest;
import com.example.jobmanagement.entity.Technician;
import com.example.jobmanagement.service.TechnicianService;
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

@RestController
@RequestMapping("/api/technicians")
@RequiredArgsConstructor
@Tag(name = "Technician Management", description = "APIs for managing technicians")
public class TechnicianController {

    private final TechnicianService technicianService;

    @Operation(summary = "Create a new technician")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Technician created successfully",
                    content = @Content(schema = @Schema(implementation = Technician.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Technician> createTechnician(
            @Parameter(description = "Technician details to create", 
                      schema = @Schema(implementation = CreateTechnicianRequest.class))
            @Valid @RequestBody CreateTechnicianRequest request) {
        return new ResponseEntity<>(technicianService.createTechnician(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a technician by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the technician",
                    content = @Content(schema = @Schema(implementation = Technician.class))),
        @ApiResponse(responseCode = "404", description = "Technician not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Technician> getTechnicianById(
            @Parameter(description = "ID of technician to fetch") @PathVariable Long id) {
        return ResponseEntity.ok(technicianService.getTechnicianById(id));
    }

    @Operation(summary = "Get all technicians")
    @ApiResponse(responseCode = "200", description = "List of all technicians",
                content = @Content(schema = @Schema(implementation = Technician.class)))
    @GetMapping
    public ResponseEntity<List<Technician>> getAllTechnicians() {
        return ResponseEntity.ok(technicianService.getAllTechnicians());
    }

    @Operation(summary = "Update a technician")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Technician updated successfully",
                    content = @Content(schema = @Schema(implementation = Technician.class))),
        @ApiResponse(responseCode = "404", description = "Technician not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Technician> updateTechnician(
            @Parameter(description = "ID of technician to update") @PathVariable Long id,
            @Parameter(description = "Updated technician details") @Valid @RequestBody Technician technician) {
        return ResponseEntity.ok(technicianService.updateTechnician(id, technician));
    }

    @Operation(summary = "Delete a technician")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Technician deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Technician not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnician(
            @Parameter(description = "ID of technician to delete") @PathVariable Long id) {
        technicianService.deleteTechnician(id);
        return ResponseEntity.noContent().build();
    }
} 