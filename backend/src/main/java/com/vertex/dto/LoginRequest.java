package com.vertex.dto;

public class LoginRequest {
    private String vrxId;
    private String password;

    // Getters and Setters
    public String getVrxId() { return vrxId; }
    public void setVrxId(String vrxId) { this.vrxId = vrxId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}