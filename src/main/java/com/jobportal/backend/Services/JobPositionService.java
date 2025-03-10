package com.jobportal.backend.Services;

import com.jobportal.backend.Entity.JobPosition;
import com.jobportal.backend.Entity.User;
import com.jobportal.backend.Enums.JobStatus;
import com.jobportal.backend.Repositories.JobPositionRepository;
import com.jobportal.backend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
public class JobPositionService {

    @Autowired
    private JobPositionRepository jobPositionRepository;

    @Autowired
    private UserRepository userRepository;


    public Page<JobPosition> getJobsByEmployer(String username, Pageable pageable) {
        return jobPositionRepository.findByEmployerUsername(username, pageable);
    }

    public Page<JobPosition> getJobsByTitleLocationAndStatus(String title, String location, JobStatus status, Pageable pageable) {
        return jobPositionRepository.findByTitleLocationAndStatus(title, location, status, pageable);
    }

    public JobPosition saveJob(JobPosition jobPosition) {
        return jobPositionRepository.save(jobPosition);
    }

    public Optional<JobPosition> getJobById(Long id) {
        return jobPositionRepository.findById(id);
    }

    public JobPosition updateJob(Long id, JobPosition updatedJob) {
        return jobPositionRepository.findById(id).map(job -> {
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setLocation(updatedJob.getLocation());
            job.setSalaryMin(updatedJob.getSalaryMin());
            job.setSalaryMax(updatedJob.getSalaryMax());
            job.setExperienceRequired(updatedJob.getExperienceRequired());
            job.setEducationLevel(updatedJob.getEducationLevel());
            job.setStatus(updatedJob.getStatus());
            return jobPositionRepository.save(job);
        }).orElse(null);
    }

    public void deleteJob(Long id) {
        jobPositionRepository.deleteById(id);
    }
}
