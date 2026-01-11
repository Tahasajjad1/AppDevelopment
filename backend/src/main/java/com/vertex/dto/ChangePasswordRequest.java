package com.vertex.dto;

public class ChangePasswordRequest {
    private String vrxId;
    private String newPassword;
    private String confirmPassword; // <--- NEW FIELD

    // Getters and Setters
    public String getVrxId() { return vrxId; }
    public void setVrxId(String vrxId) { this.vrxId = vrxId; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}