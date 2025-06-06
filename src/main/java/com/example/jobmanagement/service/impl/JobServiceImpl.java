package com.example.jobmanagement.service.impl;

import com.example.jobmanagement.entity.Job;
import com.example.jobmanagement.exception.JobNotFoundException;
import com.example.jobmanagement.exception.TechnicianNotFoundException;
import com.example.jobmanagement.repository.JobRepository;
import com.example.jobmanagement.repository.TechnicianRepository;
import com.example.jobmanagement.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of the JobService interface.
 * Provides the business logic for managing jobs in the system.
 * Handles job creation, updates, deletion, and queries.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final TechnicianRepository technicianRepository;

    /**
     * {@inheritDoc}
     * Additionally, sets the creation date to current time if not provided,
     * and validates the assigned technician exists.
     */
    @Override
    public Job createJob(Job job) {
        if (job.getTechnician() != null && job.getTechnician().getTechId() != null) {
            if (!technicianRepository.existsById(job.getTechnician().getTechId())) {
                throw new TechnicianNotFoundException(job.getTechnician().getTechId());
            }
        }
        job.setCreatedDate(LocalDateTime.now());
        if (job.getStatus() == null) {
            job.setStatus(Job.JobStatus.PENDING);
        }
        return jobRepository.save(job);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    /**
     * {@inheritDoc}
     * Additionally, validates that the technician exists before querying their jobs.
     */
    @Override
    public List<Job> getJobsByTechnicianId(Long techId) {
        if (!technicianRepository.existsById(techId)) {
            throw new TechnicianNotFoundException(techId);
        }
        return jobRepository.findByTechnicianTechId(techId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Job> getJobsByStatus(Job.JobStatus status) {
        return jobRepository.findByStatus(status);
    }

    /**
     * {@inheritDoc}
     * Additionally, sets the completion date when status changes to COMPLETED.
     */
    @Override
    public Job updateJob(Long id, Job job) {
        if (!jobRepository.existsById(id)) {
            throw new JobNotFoundException(id);
        }
        if (job.getTechnician() != null && job.getTechnician().getTechId() != null) {
            if (!technicianRepository.existsById(job.getTechnician().getTechId())) {
                throw new TechnicianNotFoundException(job.getTechnician().getTechId());
            }
        }
        job.setJobId(id);
        if (job.getStatus() == Job.JobStatus.COMPLETED && job.getCompletedDate() == null) {
            job.setCompletedDate(LocalDateTime.now());
        }
        return jobRepository.save(job);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new JobNotFoundException(id);
        }
        jobRepository.deleteById(id);
    }
} 