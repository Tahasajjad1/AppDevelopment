package com.vertex.config;

import com.vertex.model.User;
import com.vertex.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            // Check if the database is empty
            if (repository.count() == 0) {
                System.out.println("Database is empty. Seeding Default Admin...");

                // Create the Super Admin User
                User admin = new User(
                    "VRX-ADMIN",           // User ID
                    "VertexSecure2026!",   // Password (Currently Plain Text)
                    "System Administrator",// Full Name
                    "admin@vertex.com",    // Email
                    "ADMIN",               // Role
                    "IT-SECURITY"          // Department
                );

                repository.save(admin);
                System.out.println("ADMIN Created. Login with User: VRX-ADMIN | Pass: VertexSecure2026!");
            } else {
                System.out.println("Database already initialized. Skipping seeder.");
            }
        };
    }
}