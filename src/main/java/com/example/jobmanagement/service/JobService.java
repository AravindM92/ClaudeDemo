package com.example.jobmanagement.service;

import com.example.jobmanagement.entity.Job;

import java.util.List;

public interface JobService {
    Job createJob(Job job);
    Job getJobById(Long id);
    List<Job> getAllJobs();
    List<Job> getJobsByTechnicianId(Long techId);
    List<Job> getJobsByStatus(Job.JobStatus status);
    Job updateJob(Long id, Job job);
    void deleteJob(Long id);
}