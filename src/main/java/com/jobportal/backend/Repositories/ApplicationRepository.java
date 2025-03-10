package com.jobportal.backend.Repositories;

import com.jobportal.backend.Entity.Application;
import com.jobportal.backend.Entity.JobPosition;
import com.jobportal.backend.Entity.User;
import com.jobportal.backend.Enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {


    Page<Application> findByJobPositionAndStatus(JobPosition job, ApplicationStatus status, Pageable pageable);

    Page<Application> findByJobPosition(JobPosition job, Pageable pageable);


    Page<Application> findByStatus(ApplicationStatus status, Pageable pageable);

    List<Application> findBySeeker(User seeker);
}
