package com.example.jobmanagement.service;

import com.example.jobmanagement.entity.Job;
import com.example.jobmanagement.entity.Technician;
import com.example.jobmanagement.exception.JobNotFoundException;
import com.example.jobmanagement.exception.TechnicianNotFoundException;
import com.example.jobmanagement.repository.JobRepository;
import com.example.jobmanagement.repository.TechnicianRepository;
import com.example.jobmanagement.service.impl.JobServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private TechnicianRepository technicianRepository;

    @InjectMocks
    private JobServiceImpl jobService;

    private Job testJob;
    private Technician testTechnician;

    @BeforeEach
    void setUp() {
        testTechnician = new Technician();
        testTechnician.setTechId(1L);
        testTechnician.setTechName("John Doe");
        testTechnician.setDoj(LocalDate.now());

        testJob = new Job();
        testJob.setJobId(1L);
        testJob.setDescription("Test Job");
        testJob.setStatus(Job.JobStatus.PENDING);
        testJob.setCreatedDate(LocalDateTime.now());
        testJob.setTechnician(testTechnician);
    }

    @Test
    void createJob_WithValidData_ShouldReturnCreatedJob() {
        when(technicianRepository.existsById(testTechnician.getTechId())).thenReturn(true);
        when(jobRepository.save(any(Job.class))).thenReturn(testJob);

        Job createdJob = jobService.createJob(testJob);

        assertNotNull(createdJob);
        assertEquals(testJob.getJobId(), createdJob.getJobId());
        assertEquals(testJob.getDescription(), createdJob.getDescription());
        verify(jobRepository).save(any(Job.class));
    }

    @Test
    void createJob_WithInvalidTechnician_ShouldThrowException() {
        when(technicianRepository.existsById(testTechnician.getTechId())).thenReturn(false);

        assertThrows(TechnicianNotFoundException.class, () -> jobService.createJob(testJob));
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    void getJobById_WithExistingId_ShouldReturnJob() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(testJob));

        Job foundJob = jobService.getJobById(1L);

        assertNotNull(foundJob);
        assertEquals(testJob.getJobId(), foundJob.getJobId());
        assertEquals(testJob.getDescription(), foundJob.getDescription());
    }

    @Test
    void getJobById_WithNonExistingId_ShouldThrowException() {
        when(jobRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(JobNotFoundException.class, () -> jobService.getJobById(99L));
    }

    @Test
    void getAllJobs_ShouldReturnListOfJobs() {
        List<Job> jobs = Arrays.asList(testJob);
        when(jobRepository.findAll()).thenReturn(jobs);

        List<Job> foundJobs = jobService.getAllJobs();

        assertNotNull(foundJobs);
        assertEquals(1, foundJobs.size());
        assertEquals(testJob.getJobId(), foundJobs.get(0).getJobId());
    }

    @Test
    void getJobsByTechnicianId_WithValidTechnicianId_ShouldReturnJobs() {
        List<Job> jobs = Arrays.asList(testJob);
        when(technicianRepository.existsById(1L)).thenReturn(true);
        when(jobRepository.findByTechnicianTechId(1L)).thenReturn(jobs);

        List<Job> foundJobs = jobService.getJobsByTechnicianId(1L);

        assertNotNull(foundJobs);
        assertEquals(1, foundJobs.size());
        assertEquals(testJob.getJobId(), foundJobs.get(0).getJobId());
    }

    @Test
    void getJobsByTechnicianId_WithInvalidTechnicianId_ShouldThrowException() {
        when(technicianRepository.existsById(99L)).thenReturn(false);

        assertThrows(TechnicianNotFoundException.class, () -> jobService.getJobsByTechnicianId(99L));
        verify(jobRepository, never()).findByTechnicianTechId(any());
    }

    @Test
    void getJobsByStatus_ShouldReturnJobs() {
        List<Job> jobs = Arrays.asList(testJob);
        when(jobRepository.findByStatus(Job.JobStatus.PENDING)).thenReturn(jobs);

        List<Job> foundJobs = jobService.getJobsByStatus(Job.JobStatus.PENDING);

        assertNotNull(foundJobs);
        assertEquals(1, foundJobs.size());
        assertEquals(testJob.getJobId(), foundJobs.get(0).getJobId());
    }

    @Test
    void updateJob_WithValidData_ShouldReturnUpdatedJob() {
        when(jobRepository.existsById(1L)).thenReturn(true);
        when(technicianRepository.existsById(testTechnician.getTechId())).thenReturn(true);
        when(jobRepository.save(any(Job.class))).thenReturn(testJob);

        Job updatedJob = jobService.updateJob(1L, testJob);

        assertNotNull(updatedJob);
        assertEquals(testJob.getJobId(), updatedJob.getJobId());
        assertEquals(testJob.getDescription(), updatedJob.getDescription());
        verify(jobRepository).save(any(Job.class));
    }

    @Test
    void updateJob_WithNonExistingId_ShouldThrowException() {
        when(jobRepository.existsById(99L)).thenReturn(false);

        assertThrows(JobNotFoundException.class, () -> jobService.updateJob(99L, testJob));
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    void updateJob_WithInvalidTechnician_ShouldThrowException() {
        when(jobRepository.existsById(1L)).thenReturn(true);
        when(technicianRepository.existsById(testTechnician.getTechId())).thenReturn(false);

        assertThrows(TechnicianNotFoundException.class, () -> jobService.updateJob(1L, testJob));
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    void deleteJob_WithExistingId_ShouldDeleteJob() {
        when(jobRepository.existsById(1L)).thenReturn(true);
        doNothing().when(jobRepository).deleteById(1L);

        jobService.deleteJob(1L);

        verify(jobRepository).deleteById(1L);
    }

    @Test
    void deleteJob_WithNonExistingId_ShouldThrowException() {
        when(jobRepository.existsById(99L)).thenReturn(false);

        assertThrows(JobNotFoundException.class, () -> jobService.deleteJob(99L));
        verify(jobRepository, never()).deleteById(any());
    }
} 