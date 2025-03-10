package com.jobportal.backend.Controllers;

import com.jobportal.backend.Entity.JobPosition;
import com.jobportal.backend.DTOs.JobPositionDTO;
import com.jobportal.backend.Entity.User;
import com.jobportal.backend.Enums.JobStatus;
import com.jobportal.backend.Exceptions.UnauthorizedAccessException;
import com.jobportal.backend.Repositories.UserRepository;
import com.jobportal.backend.Services.JobPositionService;
import com.jobportal.backend.Utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
public class JobPositionController {

    @Autowired
    private JobPositionService jobPositionService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getJobs")
    public Page<JobPositionDTO> getJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String status) {

        Pageable pageable = PageRequest.of(page, size);

        JobStatus jobStatus = null;
        if (status != null) {
            try {
                jobStatus = JobStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                jobStatus = null;
            }
        }

        Page<JobPosition> jobs = jobPositionService.getJobsByTitleLocationAndStatus(title, location, jobStatus, pageable);

        return jobs.map(this::convertToDTO);
    }

    @PostMapping("/createJob")
    public JobPositionDTO createJob(@RequestBody JobPosition jobPosition) {
        String username = SecurityUtils.getCurrentUsername();
        User employer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        jobPosition.setEmployer(employer);

        JobPosition savedJob = jobPositionService.saveJob(jobPosition);
        return convertToDTO(savedJob);
    }
    private JobPositionDTO convertToDTO(JobPosition jobPosition) {
        JobPositionDTO jobPositionDTO = new JobPositionDTO();
        jobPositionDTO.setJobId(jobPosition.getJobId());
        jobPositionDTO.setTitle(jobPosition.getTitle());
        jobPositionDTO.setDescription(jobPosition.getDescription());
        jobPositionDTO.setLocation(jobPosition.getLocation());
        jobPositionDTO.setSalaryMin(jobPosition.getSalaryMin());
        jobPositionDTO.setSalaryMax(jobPosition.getSalaryMax());
        jobPositionDTO.setExperienceRequired(jobPosition.getExperienceRequired());
        jobPositionDTO.setEducationLevel(jobPosition.getEducationLevel());
        jobPositionDTO.setStatus(jobPosition.getStatus());

        JobPositionDTO.EmployerDTO employerDTO = new JobPositionDTO.EmployerDTO();
        employerDTO.setUserId(jobPosition.getEmployer().getUserId());
        employerDTO.setUsername(jobPosition.getEmployer().getUsername());
        employerDTO.setEmail(jobPosition.getEmployer().getEmail());
        jobPositionDTO.setEmployer(employerDTO);

        return jobPositionDTO;
    }
}
