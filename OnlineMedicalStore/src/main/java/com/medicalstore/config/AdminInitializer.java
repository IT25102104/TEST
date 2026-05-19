package com.medicalstore.config;

import com.medicalstore.entity.User;
import com.medicalstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) {
        // Create default admin if not exists
        if (userService.findByEmail("admin@medstore.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@medstore.com");
            admin.setPassword("admin123");   // plain text for now; later we'll encrypt
            admin.setFullName("Administrator");
            admin.setRole("ADMIN");
            userService.registerUser(admin);
            System.out.println("--------------------------------------------------");
            System.out.println("DEFAULT ADMIN CREATED: admin@medstore.com / admin123");
            System.out.println("--------------------------------------------------");
        }
    }
}