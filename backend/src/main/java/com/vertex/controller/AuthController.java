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
            // A. Authenticate
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getVrxId(),   
                    loginRequest.getPassword()
                )
            );

            // B. Retrieve User
            User user = userRepository.findByVrxId(loginRequest.getVrxId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // --- [FIXED NAME] Reset failed attempts if > 0 ---
            // We use 'getFailedAttempts()' because that is what is in your User.java
            if (user.getFailedAttempts() > 0) {
                user.setFailedAttempts(0);
                userRepository.save(user);
            }

            // C. Check Password Change Requirement
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
            // --- [FIXED NAME] Increment failed attempts manually ---
            
            User failedUser = userRepository.findByVrxId(loginRequest.getVrxId()).orElse(null);
            
            if (failedUser != null) {
                // Use 'getFailedAttempts()' matching your User.java
                int currentFails = failedUser.getFailedAttempts();
                int newFails = currentFails + 1;
                
                failedUser.setFailedAttempts(newFails);
                userRepository.save(failedUser); 
                
                System.out.println("Login Failed for " + failedUser.getVrxId() + ". New Attempt Count: " + newFails);
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // --- 2. CHANGE PASSWORD ENDPOINT ---
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        
        // 1. Try to find the user
        User user = userRepository.findByVrxId(request.getVrxId()).orElse(null);

        // --- NEW: SPECIFIC ERROR IF USER ID DOES NOT MATCH ---
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND) // Returns 404 Error
                .body("User ID does not exist.");
        }

        // 2. Validate: New Password == Confirm Password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match.");
        }

        // 3. Validate Password Strength (Min 8 chars)
        if (request.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest().body("New password must be at least 8 characters.");
        }

        // 4. Update Password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setOtpRequired(false); // Unlock the account
        
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully.");
    }
}