package com.medicalstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${prescription.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map /uploads/prescriptions/** to the actual folder on disk
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
        String uploadAbsolutePath = uploadPath.toUri().toString();

        registry.addResourceHandler("/uploads/prescriptions/**")
                .addResourceLocations(uploadAbsolutePath);
    }
}