package com.medicalstore.controller;

import com.medicalstore.entity.*;
import com.medicalstore.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private UserService userService;

    private User getCurrentUser(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            return userService.findByEmail(email).orElse(null);
        }
        return null;
    }

    // Show upload form
    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        return "upload-prescription";
    }

    // Handle file upload
    @PostMapping("/upload")
    public String uploadPrescription(@RequestParam("file") MultipartFile file,
                                     @RequestParam(required = false) Long orderId,
                                     HttpSession session,
                                     Model model) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        if (file.isEmpty()) {
            model.addAttribute("error", "Please select a file to upload.");
            return "upload-prescription";
        }

        try {
            Order order = null;
            // If an orderId is provided and the order belongs to this user, link them
            if (orderId != null) {
                // We'll assume OrderService is available, you can inject it
                // order = orderService.getOrderById(orderId);
                // if (!order.getUser().getId().equals(user.getId())) order = null; // security check
            }
            prescriptionService.savePrescription(user, file, order);
            model.addAttribute("success", "Prescription uploaded successfully!");
        } catch (IOException e) {
            model.addAttribute("error", "Error uploading file: " + e.getMessage());
        }
        return "upload-prescription";
    }

    // List user's prescriptions
    @GetMapping
    public String listPrescriptions(HttpSession session, Model model) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";

        List<Prescription> prescriptions = prescriptionService.getPrescriptionsForUser(user);
        model.addAttribute("prescriptions", prescriptions);
        return "prescription-list";
    }
}