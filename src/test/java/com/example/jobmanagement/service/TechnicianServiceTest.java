package com.example.jobmanagement.service;

import com.example.jobmanagement.dto.CreateTechnicianRequest;
import com.example.jobmanagement.entity.Technician;
import com.example.jobmanagement.exception.TechnicianNotFoundException;
import com.example.jobmanagement.repository.TechnicianRepository;
import com.example.jobmanagement.service.impl.TechnicianServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnicianServiceTest {

    @Mock
    private TechnicianRepository technicianRepository;

    @InjectMocks
    private TechnicianServiceImpl technicianService;

    private Technician testTechnician;
    private CreateTechnicianRequest createRequest;

    @BeforeEach
    void setUp() {
        testTechnician = new Technician();
        testTechnician.setTechId(1L);
        testTechnician.setTechName("John Doe");
        testTechnician.setDoj(LocalDate.now());

        createRequest = new CreateTechnicianRequest();
        createRequest.setTechName("John Doe");
        createRequest.setDateOfJoining(LocalDate.now());
    }

    @Test
    void createTechnician_WithValidData_ShouldReturnCreatedTechnician() {
        when(technicianRepository.save(any(Technician.class))).thenReturn(testTechnician);

        Technician createdTechnician = technicianService.createTechnician(createRequest);

        assertNotNull(createdTechnician);
        assertEquals(testTechnician.getTechId(), createdTechnician.getTechId());
        assertEquals(testTechnician.getTechName(), createdTechnician.getTechName());
        verify(technicianRepository).save(any(Technician.class));
    }

    @Test
    void getTechnicianById_WithExistingId_ShouldReturnTechnician() {
        when(technicianRepository.findById(1L)).thenReturn(Optional.of(testTechnician));

        Technician foundTechnician = technicianService.getTechnicianById(1L);

        assertNotNull(foundTechnician);
        assertEquals(testTechnician.getTechId(), foundTechnician.getTechId());
        assertEquals(testTechnician.getTechName(), foundTechnician.getTechName());
    }

    @Test
    void getTechnicianById_WithNonExistingId_ShouldThrowException() {
        when(technicianRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TechnicianNotFoundException.class, () -> technicianService.getTechnicianById(99L));
    }

    @Test
    void getAllTechnicians_ShouldReturnListOfTechnicians() {
        List<Technician> technicians = Arrays.asList(testTechnician);
        when(technicianRepository.findAll()).thenReturn(technicians);

        List<Technician> foundTechnicians = technicianService.getAllTechnicians();

        assertNotNull(foundTechnicians);
        assertEquals(1, foundTechnicians.size());
        assertEquals(testTechnician.getTechId(), foundTechnicians.get(0).getTechId());
    }

    @Test
    void updateTechnician_WithValidData_ShouldReturnUpdatedTechnician() {
        when(technicianRepository.existsById(1L)).thenReturn(true);
        when(technicianRepository.save(any(Technician.class))).thenReturn(testTechnician);

        Technician updatedTechnician = technicianService.updateTechnician(1L, testTechnician);

        assertNotNull(updatedTechnician);
        assertEquals(testTechnician.getTechId(), updatedTechnician.getTechId());
        assertEquals(testTechnician.getTechName(), updatedTechnician.getTechName());
        verify(technicianRepository).save(any(Technician.class));
    }

    @Test
    void updateTechnician_WithNonExistingId_ShouldThrowException() {
        when(technicianRepository.existsById(99L)).thenReturn(false);

        assertThrows(TechnicianNotFoundException.class, () -> technicianService.updateTechnician(99L, testTechnician));
        verify(technicianRepository, never()).save(any(Technician.class));
    }

    @Test
    void deleteTechnician_WithExistingId_ShouldDeleteTechnician() {
        when(technicianRepository.existsById(1L)).thenReturn(true);
        doNothing().when(technicianRepository).deleteById(1L);

        technicianService.deleteTechnician(1L);

        verify(technicianRepository).deleteById(1L);
    }

    @Test
    void deleteTechnician_WithNonExistingId_ShouldThrowException() {
        when(technicianRepository.existsById(99L)).thenReturn(false);

        assertThrows(TechnicianNotFoundException.class, () -> technicianService.deleteTechnician(99L));
        verify(technicianRepository, never()).deleteById(any());
    }
} 