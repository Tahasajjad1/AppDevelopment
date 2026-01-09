package com.vertex.config;

import com.vertex.model.User;
import com.vertex.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    // 1. Create the Logger
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            // Check if the database is empty
            if (repository.count() == 0) {
                // 2. Use logger.info instead of System.out.println
                logger.info("Starting Database Initialization...");
                logger.info("Database is empty. Seeding Default Admin...");

                // Create the Super Admin User
                User admin = new User(
                    "VRX-ADMIN",           // User ID
                    "VertexSecure2026!",   // Password
                    "System Administrator",// Full Name
                    "admin@vertex.com",    // Email
                    "ADMIN",               // Role
                    "IT-SECURITY"          // Department
                );

                repository.save(admin);
                
                logger.info("ADMIN Created Successfully.");
                logger.info("Login Credentials -> User: VRX-ADMIN | Pass: VertexSecure2026!");
            } else {
                logger.info("Database already initialized. Skipping seeder.");
            }
        };
    }
}