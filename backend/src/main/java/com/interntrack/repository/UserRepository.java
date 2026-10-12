package com.interntrack.repository;

import com.interntrack.model.Role;
import com.interntrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRoleOrderByNameAsc(Role role);
    long countByRole(Role role);
}
