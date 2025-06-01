package com.example.jobmanagement.service.impl;

import com.example.jobmanagement.dto.CreateTechnicianRequest;
import com.example.jobmanagement.entity.Technician;
import com.example.jobmanagement.exception.TechnicianNotFoundException;
import com.example.jobmanagement.repository.TechnicianRepository;
import com.example.jobmanagement.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository technicianRepository;

    @Override
    public List<Technician> getAllTechnicians() {
        return technicianRepository.findAll();
    }

    @Override
    public Technician getTechnicianById(Long id) {
        return technicianRepository.findById(id)
                .orElseThrow(() -> new TechnicianNotFoundException(id));
    }

    @Override
    public Technician createTechnician(CreateTechnicianRequest request) {
        Technician technician = Technician.builder()
                .techName(request.getTechName())
                .doj(request.getDateOfJoining())
                .build();
        return technicianRepository.save(technician);
    }

    @Override
    public Technician updateTechnician(Long id, Technician technician) {
        if (!technicianRepository.existsById(id)) {
            throw new TechnicianNotFoundException(id);
        }
        technician.setTechId(id);
        return technicianRepository.save(technician);
    }

    @Override
    public void deleteTechnician(Long id) {
        if (!technicianRepository.existsById(id)) {
            throw new TechnicianNotFoundException(id);
        }
        technicianRepository.deleteById(id);
    }
}