package com.vertex.controller;

import com.vertex.dto.LoginRequest;
import com.vertex.model.User;
import com.vertex.service.AuthService;
import org.slf4j.Logger;                // 1. Import Logger
import org.slf4j.LoggerFactory;         // 2. Import Factory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    // 3. Define the Logger for this class
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        
        // 4. Log the incoming request
        logger.info("🔹 Login Attempt Received for User: {}", request.getVrxId());

        User user = authService.authenticate(request.getVrxId(), request.getPassword());

        if (user != null) {
            // 5. Log Success (INFO level)
            logger.info("Login SUCCESS for User: {} ({})", user.getVrxId(), user.getRole());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Welcome to Vertex Spectrum");
            response.put("userId", user.getVrxId());
            response.put("role", user.getRole());
            response.put("name", user.getFullName());
            
            return ResponseEntity.ok(response);
        } else {
            // 6. Log Failure (WARN level - Important for security audits)
            logger.warn("Login FAILED for User: {} - Invalid Credentials", request.getVrxId());

            Map<String, String> error = new HashMap<>();
            error.put("status", "ERROR");
            error.put("message", "Invalid Credentials or Account Locked");
            
            return ResponseEntity.status(401).body(error);
        }
    }
}