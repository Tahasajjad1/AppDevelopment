package com.vertex.controller;

import com.vertex.model.User;
import com.vertex.repository.UserRepository;
import com.vertex.security.JwtTokenProvider; 
import com.vertex.dto.LoginRequest;
import com.vertex.dto.LoginResponse;
import com.vertex.dto.ChangePasswordRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    // --- 1. LOGIN ENDPOINT ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // A. Authenticate using VRX-ID (Not Username)
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getVrxId(),   // <--- CHANGED THIS from getUsername()
                    loginRequest.getPassword()
                )
            );

            // B. Retrieve User by VRX-ID to check status
            User user = userRepository.findByVrxId(loginRequest.getVrxId()) // <--- CHANGED THIS from findByEmail/getUsername
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // C. CHECK IF PASSWORD CHANGE IS REQUIRED
            if (user.isOtpRequired()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new LoginResponse(
                        "PASSWORD_CHANGE_REQUIRED", 
                        "You must change your password before proceeding."
                    ));
            }

            // D. Generate Token
            String jwt = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new LoginResponse("SUCCESS", jwt));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // --- 2. CHANGE PASSWORD ENDPOINT ---
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        
        // Find by Email is correct here because ChangePasswordRequest uses email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            return ResponseEntity.badRequest().body("Current password is incorrect.");
        }

        if (request.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest().body("New password must be at least 8 characters.");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setOtpRequired(false); 
        
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully. Please login again.");
    }
}