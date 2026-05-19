package com.medicalstore.service;

import com.medicalstore.entity.*;
import com.medicalstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    // Gets the upload folder path from application.properties
    @Value("${prescription.upload.dir}")
    private String uploadDir;

    public Prescription savePrescription(User user, MultipartFile file, Order order) throws IOException {
        // Create the uploads folder if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate a unique file name to avoid overwriting
        String originalFilename = file.getOriginalFilename();
        String storedFileName = UUID.randomUUID().toString() + "_" + originalFilename;

        // Save the file on the server
        Path filePath = uploadPath.resolve(storedFileName);
        Files.copy(file.getInputStream(), filePath);

        // Create and save the prescription record
        Prescription prescription = new Prescription();
        prescription.setUser(user);
        prescription.setOriginalFileName(originalFilename);
        prescription.setStoredFileName(storedFileName);
        prescription.setFilePath(filePath.toString());
        prescription.setUploadDate(LocalDateTime.now());
        prescription.setOrder(order);   // can be null

        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPrescriptionsForUser(User user) {
        return prescriptionRepository.findByUserOrderByUploadDateDesc(user);
    }

    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
    }
}