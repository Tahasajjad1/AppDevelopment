package com.vertex;

import org.slf4j.Logger;                // 1. Import Logger
import org.slf4j.LoggerFactory;         // 2. Import Factory
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Entry Point for Vertex Spectrum Backend.
 * This class triggers the auto-configuration of Oracle and Web Server.
 */
@SpringBootApplication
public class VertexBackendApplication {

    // 3. Define the Logger
    private static final Logger logger = LoggerFactory.getLogger(VertexBackendApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(VertexBackendApplication.class, args);
        
        // 4. Log the startup success message
        logger.info("=========================================================");
        logger.info("Vertex Spectrum Backend is RUNNING and Ready!");
        logger.info("=========================================================");
    }

}