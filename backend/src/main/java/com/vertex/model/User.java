package com.vertex.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VX_USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- CORE CREDENTIALS ---
    @Column(name = "VRX_ID", unique = true, nullable = false)
    private String vrxId;

    @Column(name = "PASSWORD_HASH", nullable = false)
    private String passwordHash;

    // --- PERSONAL DETAILS ---
    @Column(name = "FULL_NAME")
    private String fullName;      // e.g., "Sarah Connor"

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    // --- ACCESS CONTROL ---
    @Column(name = "ROLE", nullable = false)
    private String role;          // e.g., "ADMIN", "USER", "MANAGER"

    @Column(name = "DEPARTMENT")
    private String department;    // e.g., "IT", "FINANCE", "HR"

    @Column(name = "STATUS")
    private String status;        // "ACTIVE", "LOCKED", "INACTIVE"

    // --- SECURITY & AUDIT ---
    @Column(name = "FAILED_ATTEMPTS")
    private int failedAttempts = 0;  // To lock account after 3 wrong tries

    @Column(name = "LAST_LOGIN")
    private LocalDateTime lastLogin;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    // --- OTP & FORCED PASSWORD CHANGE FIELDS (NEW) ---
    @Column(name = "OTP_REQUIRED")
    private boolean otpRequired = false; // If true, user MUST change password

    @Column(name = "OTP_CODE")
    private String otpCode;              // Stores the temporary OTP code

    @Column(name = "OTP_EXPIRY_TIME")
    private LocalDateTime otpExpiryTime; // When the OTP expires

    // --- CONSTRUCTORS ---
    public User() {}

    public User(String vrxId, String passwordHash, String fullName, String email, String role, String department) {
        this.vrxId = vrxId;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.department = department;
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
        this.failedAttempts = 0;
        this.otpRequired = false; // Default to false
    }

    // --- GETTERS AND SETTERS ---
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVrxId() { return vrxId; }
    public void setVrxId(String vrxId) { this.vrxId = vrxId; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    // NOTE: In Spring Security, this is usually mapped to getPassword()
    public String getPassword() { return passwordHash; } 

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // --- NEW GETTERS/SETTERS FOR OTP ---
    
    public boolean isOtpRequired() { return otpRequired; }
    public void setOtpRequired(boolean otpRequired) { this.otpRequired = otpRequired; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public LocalDateTime getOtpExpiryTime() { return otpExpiryTime; }
    public void setOtpExpiryTime(LocalDateTime otpExpiryTime) { this.otpExpiryTime = otpExpiryTime; }
}