package com.vertex.controller;

import com.vertex.model.User;
import com.vertex.repository.UserRepository;
import com.vertex.security.JwtTokenProvider; // Ensure you have your JWT provider here
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
            // A. Authenticate the user (Checks username & password match)
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );

            // B. Retrieve User to check status
            User user = userRepository.findByEmail(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // C. CHECK IF PASSWORD CHANGE IS REQUIRED
            if (user.isOtpRequired()) {
                // STOP! Do not generate a token. Return a specific warning.
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new LoginResponse(
                        "PASSWORD_CHANGE_REQUIRED", 
                        "You must change your password before proceeding."
                    ));
            }

            // D. If clean, generate Token
            String jwt = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new LoginResponse("SUCCESS", jwt));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // --- 2. CHANGE PASSWORD ENDPOINT ---
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        
        // A. Find the user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // B. Verify the OLD password matches (Security Check)
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            return ResponseEntity.badRequest().body("Current password is incorrect.");
        }

        // C. Validate NEW password strength (Simple check)
        if (request.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest().body("New password must be at least 8 characters.");
        }

        // D. Update Password & Reset Flag
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setOtpRequired(false); // Unlock the account
        
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully. Please login again.");
    }
}