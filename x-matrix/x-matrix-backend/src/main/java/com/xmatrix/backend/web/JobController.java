package com.xmatrix.backend.web;

import com.xmatrix.backend.DTOs.JobRequestDTO;
import com.xmatrix.backend.DTOs.JobResponseDTO;
import com.xmatrix.backend.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<JobResponseDTO> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable Long id) {
        Optional<JobResponseDTO> job = jobService.getJobById(id);
        return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public JobResponseDTO createJob(@RequestBody JobRequestDTO requestDTO) {
        return jobService.createJob(requestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDTO> updateJob(@PathVariable Long id, @RequestBody JobRequestDTO requestDTO) {
        return ResponseEntity.ok(jobService.updateJob(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteJob(@PathVariable Long id) {
        jobService.softDeleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<Void> restoreJob(@PathVariable Long id) {
        jobService.restoreJob(id);
        return ResponseEntity.noContent().build();
    }
}
