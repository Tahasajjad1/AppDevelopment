package com.vertex.repository;

import com.vertex.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // This "Magic Method" tells Spring to write the SQL query for you.
    // It roughly translates to: "SELECT * FROM VX_USERS WHERE VRX_ID = ?"
    Optional<User> findByVrxId(String vrxId);
    
    // Optional: Useful to prevent duplicate emails during registration
    boolean existsByVrxId(String vrxId);
}