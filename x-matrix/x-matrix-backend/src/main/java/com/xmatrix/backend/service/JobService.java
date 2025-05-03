package com.xmatrix.backend.service;

import com.xmatrix.backend.DTOs.JobRequestDTO;
import com.xmatrix.backend.DTOs.JobResponseDTO;
import com.xmatrix.backend.entity.Job;
import com.xmatrix.backend.mappers.JobMapper;
import com.xmatrix.backend.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public List<JobResponseDTO> getAllJobs() {
        List<Job> jobs = jobRepository.findAllActiveAndOrderByCreatedAtDesc();
        return jobs.stream().map(JobMapper.INSTANCE::toDto).toList();
    }

    public Optional<JobResponseDTO> getJobById(Long id) {
        return jobRepository.findById(id)
                .filter(job -> !job.getDeleted())
                .map(JobMapper.INSTANCE::toDto);
    }

    public JobResponseDTO createJob(JobRequestDTO requestDTO) {
        Job job = JobMapper.INSTANCE.toEntity(requestDTO);
        job.setDeleted(false);
        job = jobRepository.save(job);
        return JobMapper.INSTANCE.toDto(job);
    }

    public JobResponseDTO updateJob(Long id, JobRequestDTO requestDTO) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setName(requestDTO.getName());
        job = jobRepository.save(job);
        return JobMapper.INSTANCE.toDto(job);
    }

    public void softDeleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setDeleted(true);
        jobRepository.save(job);
    }

    public void restoreJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setDeleted(false);
        jobRepository.save(job);
    }
}
