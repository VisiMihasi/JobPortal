package com.jobportal.backend.Services;

import com.jobportal.backend.Controllers.ApplicationController;
import com.jobportal.backend.Entity.Application;
import com.jobportal.backend.Entity.JobPosition;
import com.jobportal.backend.Entity.User;
import com.jobportal.backend.Enums.ApplicationStatus;
import com.jobportal.backend.Repositories.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> getApplicationsBySeeker(User seeker) {
        return applicationRepository.findBySeeker(seeker);
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public Application submitApplication(Application application) {
        return applicationRepository.save(application);
    }

    public Page<Application> getApplicationsByJobAndStatus(JobPosition job, ApplicationStatus status, Pageable pageable) {
        return applicationRepository.findByJobPositionAndStatus(job, status, pageable);
    }

    public Page<Application> getApplicationsByJob(JobPosition job, Pageable pageable) {
        return applicationRepository.findByJobPosition(job, pageable);
    }

    public Application updateApplicationStatusWithReview(Application application, ApplicationStatus status, String review) {
        logger.info("Current status of application {}: {}", application.getApplicationId(), application.getStatus());

        if (review == null || review.isEmpty()) {
            throw new RuntimeException("Review cannot be empty.");
        }

        if (application.getStatus() == ApplicationStatus.PENDING) {
            logger.info("Application is in PENDING status, updating to status: {}", status);
            application.setStatus(status);
            application.setReview(review);
        } else {
            logger.error("Cannot update application {} as it is not in PENDING status. Current status: {}", application.getApplicationId(), application.getStatus());
            throw new RuntimeException("Cannot change status or review for a non-PENDING application.");
        }

        return applicationRepository.save(application);
    }




    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
}
