package com.vertex.service;

import com.vertex.model.User;
import com.vertex.repository.UserRepository;
import org.slf4j.Logger;     // <--- 1. Import SLF4J
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    // 2. Create the Logger
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String vrxId, String rawPassword) {
        // 3. Use logger instead of System.out
        logger.info("Authentication attempt for UserID: {}", vrxId);
        
        Optional<User> userOptional = userRepository.findByVrxId(vrxId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (rawPassword.equals(user.getPasswordHash())) {
                logger.info("Login SUCCESS for user: {}", vrxId); // Goes to system.log
                return user;
            } else {
                logger.warn("Login FAILED (Wrong Password) for user: {}", vrxId);
            }
        } else {
            logger.warn("Login FAILED (User not found): {}", vrxId);
        }
        return null;
    }
}