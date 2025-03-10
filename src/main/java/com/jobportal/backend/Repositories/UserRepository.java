package com.jobportal.backend.Repositories;

import com.jobportal.backend.Entity.JobPosition;
import com.jobportal.backend.Entity.User;
import com.jobportal.backend.Enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByUsername(String username);

    Page<User> findByUsernameContainingOrEmailContainingOrRole(Pageable pageable, String username, String email, UserRole role);

    Page<User> findByRole(Pageable pageable, UserRole role);

    Page<User> findByUsernameContainingOrEmailContaining(Pageable pageable, String username, String email);

    Page<User> findByUsernameContaining(Pageable pageable, String username);

    Page<User> findByEmailContaining(Pageable pageable, String email);
}


