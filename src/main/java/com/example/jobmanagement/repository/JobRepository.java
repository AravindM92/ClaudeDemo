package com.example.jobmanagement.repository;

import com.example.jobmanagement.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Job entity.
 * Provides data access methods for jobs in the database.
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    /**
     * Finds all jobs assigned to a specific technician.
     *
     * @param techId the ID of the technician
     * @return list of jobs assigned to the technician
     */
    List<Job> findByTechnicianTechId(Long techId);

    /**
     * Finds all jobs with a specific status.
     *
     * @param status the status to filter by
     * @return list of jobs with the specified status
     */
    List<Job> findByStatus(Job.JobStatus status);
} 