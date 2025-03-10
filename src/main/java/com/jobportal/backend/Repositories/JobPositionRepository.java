package com.jobportal.backend.Repositories;

import com.jobportal.backend.Entity.JobPosition;
import com.jobportal.backend.Enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {

    Page<JobPosition> findByStatus(JobStatus status, Pageable pageable);

    Page<JobPosition> findByEmployerUsername(String username, Pageable pageable);

    @Query("SELECT jp FROM JobPosition jp WHERE " +
            "(LOWER(jp.title) LIKE LOWER(CONCAT('%', :title, '%')) OR :title IS NULL) AND " +
            "(LOWER(jp.location) LIKE LOWER(CONCAT('%', :location, '%')) OR :location IS NULL) AND " +
            "(jp.status = :status OR :status IS NULL)")
    Page<JobPosition> findByTitleLocationAndStatus(
            @Param("title") String title,
            @Param("location") String location,
            @Param("status") JobStatus status,
            Pageable pageable);

}
