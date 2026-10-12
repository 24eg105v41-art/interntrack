package com.interntrack.repository;

import com.interntrack.model.ApplicationStatus;
import com.interntrack.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InternshipRepository extends JpaRepository<Internship, Long> {

    List<Internship> findByUser_UserIdOrderByDeadlineAsc(Long userId);

    Optional<Internship> findByInternshipIdAndUser_UserId(Long internshipId, Long userId);

    List<Internship> findAllByOrderByDeadlineAsc();

    List<Internship> findByStatusOrderByDeadlineAsc(ApplicationStatus status);

    long countByStatus(ApplicationStatus status);

    long countByUser_UserId(Long userId);
}
