package com.example.jobmanagement.controller;

import com.example.jobmanagement.config.TestSecurityConfig;
import com.example.jobmanagement.entity.Job;
import com.example.jobmanagement.service.JobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the Job Controller.
 * Tests all REST endpoints and their responses using MockMvc.
 * Uses TestSecurityConfig to disable security for testing.
 */
@WebMvcTest(JobController.class)
@Import(TestSecurityConfig.class)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @Autowired
    private ObjectMapper objectMapper;

    private Job testJob;
    private List<Job> testJobs;

    /**
     * Sets up test data before each test.
     * Creates sample job and technician data for testing.
     */
    @BeforeEach
    void setUp() {
        testJob = new Job();
        testJob.setJobId(1L);
        testJob.setDescription("Test Job");
        testJob.setStatus(Job.JobStatus.PENDING);
        testJob.setCreatedDate(LocalDateTime.now());

        Job testJob2 = new Job();
        testJob2.setJobId(2L);
        testJob2.setDescription("Test Job 2");
        testJob2.setStatus(Job.JobStatus.IN_PROGRESS);
        testJob2.setCreatedDate(LocalDateTime.now());

        testJobs = Arrays.asList(testJob, testJob2);
    }

    /**
     * Tests that getAllJobs endpoint returns a list of jobs successfully.
     * Verifies the response status, content type, and response body.
     */
    @Test
    void getAllJobs_ShouldReturnListOfJobs() throws Exception {
        when(jobService.getAllJobs()).thenReturn(testJobs);

        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].jobId").value(testJob.getJobId()))
                .andExpect(jsonPath("$[0].description").value(testJob.getDescription()));
    }

    /**
     * Tests that getJobById endpoint returns a specific job successfully.
     * Verifies the response status, content type, and job details.
     */
    @Test
    void getJobById_ShouldReturnJob() throws Exception {
        when(jobService.getJobById(1L)).thenReturn(testJob);

        mockMvc.perform(get("/api/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.jobId").value(testJob.getJobId()))
                .andExpect(jsonPath("$.description").value(testJob.getDescription()));
    }

    /**
     * Tests that getJobsByTechnicianId endpoint returns jobs for a technician.
     * Verifies the response status, content type, and job list.
     */
    @Test
    void getJobsByTechnicianId_ShouldReturnJobs() throws Exception {
        when(jobService.getJobsByTechnicianId(1L)).thenReturn(testJobs);

        mockMvc.perform(get("/api/jobs/technician/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    /**
     * Tests that getJobsByStatus endpoint returns jobs with specific status.
     * Verifies the response status, content type, and filtered jobs.
     */
    @Test
    void getJobsByStatus_ShouldReturnJobs() throws Exception {
        when(jobService.getJobsByStatus(Job.JobStatus.PENDING)).thenReturn(Arrays.asList(testJob));

        mockMvc.perform(get("/api/jobs/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    /**
     * Tests that createJob endpoint creates a new job successfully.
     * Verifies the response status is CREATED and job details are correct.
     */
    @Test
    void createJob_ShouldReturnCreatedJob() throws Exception {
        when(jobService.createJob(any(Job.class))).thenReturn(testJob);

        mockMvc.perform(post("/api/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testJob)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.jobId").value(testJob.getJobId()))
                .andExpect(jsonPath("$.description").value(testJob.getDescription()));
    }

    /**
     * Tests that updateJob endpoint updates an existing job successfully.
     * Verifies the response status and updated job details.
     */
    @Test
    void updateJob_ShouldReturnUpdatedJob() throws Exception {
        when(jobService.updateJob(eq(1L), any(Job.class))).thenReturn(testJob);

        mockMvc.perform(put("/api/jobs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testJob)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.jobId").value(testJob.getJobId()))
                .andExpect(jsonPath("$.description").value(testJob.getDescription()));
    }

    /**
     * Tests that deleteJob endpoint deletes a job successfully.
     * Verifies the response status is NO_CONTENT.
     */
    @Test
    void deleteJob_ShouldReturnNoContent() throws Exception {
        doNothing().when(jobService).deleteJob(1L);

        mockMvc.perform(delete("/api/jobs/1"))
                .andExpect(status().isNoContent());
    }
} 