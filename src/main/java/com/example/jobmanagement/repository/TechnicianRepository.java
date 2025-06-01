package com.example.jobmanagement.repository;

import com.example.jobmanagement.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    // Add custom query methods if needed
} 