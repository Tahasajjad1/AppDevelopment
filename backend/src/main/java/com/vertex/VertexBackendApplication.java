package com.vertex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Entry Point for Vertex Spectrum Backend.
 * This class triggers the auto-configuration of Oracle and Web Server.
 */
@SpringBootApplication
public class VertexBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VertexBackendApplication.class, args);
        System.out.println("Backend is RUNNING!");
    }

}