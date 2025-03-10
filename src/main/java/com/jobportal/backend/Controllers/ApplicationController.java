package com.jobportal.backend.Controllers;

import com.jobportal.backend.DTOs.ApplicationDTO;
import com.jobportal.backend.Entity.Application;
import com.jobportal.backend.Entity.JobPosition;
import com.jobportal.backend.Entity.User;
import com.jobportal.backend.Enums.ApplicationStatus;
import com.jobportal.backend.Services.ApplicationService;
import com.jobportal.backend.Services.JobPositionService;
import com.jobportal.backend.Services.UserService;
import com.jobportal.backend.Utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {


    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JobPositionService jobPositionService;

    @Autowired
    private UserService userService;

    @PostMapping("/apply/{jobId}")
    public ApplicationDTO applyForJob(@PathVariable Long jobId, @RequestBody ApplicationDTO applicationDTO) {

        if (!SecurityUtils.isJobSeeker()) {
            throw new RuntimeException("You must be a job seeker to apply for a job.");
        }

        JobPosition job = jobPositionService.getJobById(jobId)
                .orElseThrow(() -> new RuntimeException("Job position not found"));

        String currentUsername = SecurityUtils.getCurrentUsername();
        User seeker = userService.getUserByUsername(currentUsername);

        Application application = new Application();
        application.setJobPosition(job);
        application.setSeeker(seeker);
        application.setStatus(ApplicationStatus.PENDING);
        application.setApplyDate(LocalDateTime.now());
        application.setResume(applicationDTO.getResume());
        Application savedApplication = applicationService.submitApplication(application);

        return convertToDTO(savedApplication);
    }

    @GetMapping("/myApplications")
    public List<ApplicationDTO> getApplicationsForJobSeeker(Principal principal) {
        if (!SecurityUtils.isJobSeeker()) {
            throw new RuntimeException("You must be a job seeker to view your applications.");
        }

        String currentUsername = SecurityUtils.getCurrentUsername();
        User seeker = userService.getUserByUsername(currentUsername);

        List<Application> applications = applicationService.getApplicationsBySeeker(seeker);

        return applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/myApplicationsForJob/{jobId}")
    public Page<ApplicationDTO> getApplicationsForEmployerJob(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ApplicationStatus status,
            Principal principal) {

        if (!SecurityUtils.isEmployer()) {
            throw new RuntimeException("You must be an employer to view applications for your jobs.");
        }

        String currentUsername = SecurityUtils.getCurrentUsername();
        User employer = userService.getUserByUsername(currentUsername);

        JobPosition job = jobPositionService.getJobById(jobId)
                .orElseThrow(() -> new RuntimeException("Job position not found"));

        if (!job.getEmployer().equals(employer)) {
            throw new RuntimeException("You do not have permission to view applications for this job.");
        }

        Page<Application> applications;
        if (status != null) {
            applications = applicationService.getApplicationsByJobAndStatus(job, status, PageRequest.of(page, size));
        } else {
            applications = applicationService.getApplicationsByJob(job, PageRequest.of(page, size));
        }

        return applications.map(this::convertToDTO);
    }


    @PutMapping("/updateStatus/{applicationId}")
    public ApplicationDTO updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody ApplicationDTO applicationDTO,
            Principal principal) {

        logger.info("Received status update for applicationId {}: {}", applicationId, applicationDTO.getStatus());

        if (!SecurityUtils.isEmployer()) {
            throw new RuntimeException("You must be an employer to update application statuses.");
        }

        String currentUsername = SecurityUtils.getCurrentUsername();
        User employer = userService.getUserByUsername(currentUsername);

        Application application = applicationService.getApplicationById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        JobPosition job = application.getJobPosition();
        if (!job.getEmployer().equals(employer)) {
            throw new RuntimeException("You do not have permission to update this application status.");
        }

        ApplicationStatus status = applicationDTO.getStatus();
        String review = applicationDTO.getReview();

        if (review == null || review.isEmpty()) {
            throw new RuntimeException("Review is mandatory when changing the status.");
        }

        application = applicationService.updateApplicationStatusWithReview(application, status, review);
        return convertToDTO(application);
    }

    private ApplicationDTO convertToDTO(Application application) {
        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setApplicationId(application.getApplicationId());
        applicationDTO.setJobId(application.getJobPosition().getJobId());
        applicationDTO.setSeekerId(application.getSeeker().getUserId());
        applicationDTO.setStatus(application.getStatus());
        applicationDTO.setApplyDate(application.getApplyDate());
        applicationDTO.setResume(application.getResume());
        applicationDTO.setSeekerUsername(application.getSeeker().getUsername());
        applicationDTO.setSeekerEmail(application.getSeeker().getEmail());
        applicationDTO.setReview(application.getReview());

        return applicationDTO;
    }

}

