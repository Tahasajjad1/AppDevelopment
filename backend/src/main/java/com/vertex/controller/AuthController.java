package com.vertex.controller;

import com.vertex.dto.LoginRequest;
import com.vertex.model.User;
import com.vertex.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
// @CrossOrigin allows Angular (port 4200) to talk to Spring Boot (port 8080)
@CrossOrigin(origins = "http://localhost:4200") 
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        
        System.out.println("🔹 Login Attempt Received for: " + request.getVrxId());

        User user = authService.authenticate(request.getVrxId(), request.getPassword());

        if (user != null) {
            // SUCCESS: Return the user info (excluding the password!)
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Welcome to Vertex Spectrum");
            response.put("userId", user.getVrxId());
            response.put("role", user.getRole());
            response.put("name", user.getFullName());
            
            return ResponseEntity.ok(response);
        } else {
            // FAILURE
            Map<String, String> error = new HashMap<>();
            error.put("status", "ERROR");
            error.put("message", "Invalid Credentials or Account Locked");
            
            return ResponseEntity.status(401).body(error);
        }
    }
}