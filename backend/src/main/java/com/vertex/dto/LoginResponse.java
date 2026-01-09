package com.vertex.dto;

public class LoginResponse {
    private String status;
    private String message; // or 'token'

    public LoginResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters
    public String getStatus() { return status; }
    public String getMessage() { return message; }
}