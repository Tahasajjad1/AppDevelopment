package com.vertex.repository;

import com.vertex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Used for Login (matches your LoginRequest)
    Optional<User> findByVrxId(String vrxId);
    
    // Used for Registration checks
    boolean existsByVrxId(String vrxId);

    // --- ADD THIS MISSING METHOD ---
    // Used by AuthController.changePassword() to find user by email
    Optional<User> findByEmail(String email);
}