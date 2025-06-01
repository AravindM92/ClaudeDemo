package com.example.jobmanagement.service;

import com.example.jobmanagement.dto.CreateTechnicianRequest;
import com.example.jobmanagement.entity.Technician;

import java.util.List;

public interface TechnicianService {
    Technician createTechnician(CreateTechnicianRequest request);
    Technician getTechnicianById(Long id);
    List<Technician> getAllTechnicians();
    Technician updateTechnician(Long id, Technician technician);
    void deleteTechnician(Long id);
} 