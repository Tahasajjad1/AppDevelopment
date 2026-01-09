package com.vertex.config;

import com.vertex.model.User;
import com.vertex.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class DataInitializer {

    // 1. Create the Logger
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    // Helper method to generate a random secure password
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    @Bean
    // Inject PasswordEncoder to encrypt the password before saving
    CommandLineRunner initDatabase(UserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if the database is empty
            if (repository.count() == 0) {
                logger.info("Starting Database Initialization...");
                logger.info("Database is empty. Seeding Default Admin...");

                // 2. Generate a Random Password (10 characters long)
                String randomPassword = generateRandomPassword(10);

                // Create the Super Admin User
                User admin = new User(
                    "VRX-ADMIN",
                    passwordEncoder.encode(randomPassword), // Encrypt the password!
                    "System Administrator",
                    "admin@vertex.com",
                    "ADMIN",
                    "IT-SECURITY"
                );

                // 3. Force user to change password on first login
                // Make sure your User entity has this field!
                admin.setOtpRequired(true);

                repository.save(admin);
                
                logger.info("ADMIN Created Successfully.");
                
                // 4. Print the RAW password to the console so you can copy it
                logger.info("----------------------------------------------------------");
                logger.info("TEMPORARY LOGIN CREDENTIALS:");
                logger.info("User: VRX-ADMIN");
                logger.info("Pass: " + randomPassword);
                logger.info("NOTE: User must change password after first login.");
                logger.info("----------------------------------------------------------");
            } else {
                logger.info("Database already initialized. Skipping seeder.");
            }
        };
    }
}