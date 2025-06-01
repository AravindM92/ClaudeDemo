package com.example.jobmanagement.repository;

import com.example.jobmanagement.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByTechnicianTechId(Long techId);
    List<Job> findByStatus(Job.JobStatus status);
} 