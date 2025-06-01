package com.example.jobmanagement.controller;

import com.example.jobmanagement.config.TestSecurityConfig;
import com.example.jobmanagement.dto.CreateTechnicianRequest;
import com.example.jobmanagement.entity.Technician;
import com.example.jobmanagement.service.TechnicianService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TechnicianController.class)
@Import(TestSecurityConfig.class)
class TechnicianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TechnicianService technicianService;

    @Autowired
    private ObjectMapper objectMapper;

    private Technician testTechnician;
    private CreateTechnicianRequest createRequest;
    private List<Technician> testTechnicians;

    @BeforeEach
    void setUp() {
        testTechnician = new Technician();
        testTechnician.setTechId(1L);
        testTechnician.setTechName("John Doe");
        testTechnician.setDoj(LocalDate.now());

        createRequest = new CreateTechnicianRequest();
        createRequest.setTechName("John Doe");
        createRequest.setDateOfJoining(LocalDate.now());

        Technician testTechnician2 = new Technician();
        testTechnician2.setTechId(2L);
        testTechnician2.setTechName("Jane Smith");
        testTechnician2.setDoj(LocalDate.now());

        testTechnicians = Arrays.asList(testTechnician, testTechnician2);
    }

    @Test
    void createTechnician_ShouldReturnCreatedTechnician() throws Exception {
        when(technicianService.createTechnician(any(CreateTechnicianRequest.class))).thenReturn(testTechnician);

        mockMvc.perform(post("/api/technicians")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.techId").value(testTechnician.getTechId()))
                .andExpect(jsonPath("$.techName").value(testTechnician.getTechName()));
    }

    @Test
    void getTechnicianById_ShouldReturnTechnician() throws Exception {
        when(technicianService.getTechnicianById(1L)).thenReturn(testTechnician);

        mockMvc.perform(get("/api/technicians/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.techId").value(testTechnician.getTechId()))
                .andExpect(jsonPath("$.techName").value(testTechnician.getTechName()));
    }

    @Test
    void getAllTechnicians_ShouldReturnListOfTechnicians() throws Exception {
        when(technicianService.getAllTechnicians()).thenReturn(testTechnicians);

        mockMvc.perform(get("/api/technicians"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].techId").value(testTechnician.getTechId()))
                .andExpect(jsonPath("$[0].techName").value(testTechnician.getTechName()));
    }

    @Test
    void updateTechnician_ShouldReturnUpdatedTechnician() throws Exception {
        when(technicianService.updateTechnician(eq(1L), any(Technician.class))).thenReturn(testTechnician);

        mockMvc.perform(put("/api/technicians/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTechnician)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.techId").value(testTechnician.getTechId()))
                .andExpect(jsonPath("$.techName").value(testTechnician.getTechName()));
    }

    @Test
    void deleteTechnician_ShouldReturnNoContent() throws Exception {
        doNothing().when(technicianService).deleteTechnician(1L);

        mockMvc.perform(delete("/api/technicians/1"))
                .andExpect(status().isNoContent());
    }
} 