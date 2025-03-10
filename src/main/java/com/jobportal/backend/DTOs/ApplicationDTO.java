package com.jobportal.backend.DTOs;

import com.jobportal.backend.Enums.ApplicationStatus;
import java.time.LocalDateTime;

public class ApplicationDTO {

    public static class ApplicationStatusUpdateDTO {
        private ApplicationStatus status;
        private String review;

        public ApplicationStatus getStatus() {
            return status;
        }

        public void setStatus(ApplicationStatus status) {
            this.status = status;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }
    }

    private Long applicationId;
    private Long jobId;
    private Long seekerId;
    private String seekerUsername;
    private String seekerEmail;
    private ApplicationStatus status;
    private LocalDateTime applyDate;
    private String resume;
    private String review;

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(Long seekerId) {
        this.seekerId = seekerId;
    }

    public String getSeekerUsername() {
        return seekerUsername;
    }

    public void setSeekerUsername(String seekerUsername) {
        this.seekerUsername = seekerUsername;
    }

    public String getSeekerEmail() {
        return seekerEmail;
    }

    public void setSeekerEmail(String seekerEmail) {
        this.seekerEmail = seekerEmail;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDateTime applyDate) {
        this.applyDate = applyDate;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
