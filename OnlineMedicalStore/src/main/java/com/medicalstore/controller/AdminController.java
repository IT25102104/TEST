package com.medicalstore.controller;

import com.medicalstore.entity.*;
import com.medicalstore.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private OrderService orderService;

    // --- Helper: check admin login ---
    private User getLoggedAdmin(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            User user = userService.findByEmail(email).orElse(null);
            if (user != null && "ADMIN".equalsIgnoreCase(user.getRole())) {
                return user;
            }
        }
        return null;
    }

    // ===================== DASHBOARD =====================
    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        User admin = getLoggedAdmin(session);
        if (admin == null) {
            return "redirect:/login";
        }

        long totalUsers = userService.countUsers();           // We'll add these methods
        long totalMedicines = medicineService.countMedicines();
        long totalOrders = orderService.countOrders();
        double totalRevenue = orderService.getTotalRevenue();

        model.addAttribute("admin", admin);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalMedicines", totalMedicines);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalRevenue", totalRevenue);

        return "admin/dashboard";
    }

    // ===================== MANAGE MEDICINES =====================
    @GetMapping("/medicines")
    public String manageMedicines(HttpSession session, Model model) {
        if (getLoggedAdmin(session) == null) return "redirect:/login";
        model.addAttribute("medicines", medicineService.getAllMedicines());
        return "admin/medicines";
    }

    // Add / Edit medicine reuse the same form from MedicineController
    // So we can redirect to /medicines/add or /medicines/edit/{id}
    // But we'll also add admin-specific delete with confirmation handled there.

    // ===================== MANAGE USERS =====================
    @GetMapping("/users")
    public String manageUsers(HttpSession session, Model model) {
        if (getLoggedAdmin(session) == null) return "redirect:/login";
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (getLoggedAdmin(session) == null) return "redirect:/login";
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // ===================== MANAGE ORDERS =====================
    @GetMapping("/orders")
    public String manageOrders(HttpSession session, Model model) {
        if (getLoggedAdmin(session) == null) return "redirect:/login";
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @PostMapping("/orders/update-status/{id}")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam String status,
                                    HttpSession session) {
        if (getLoggedAdmin(session) == null) return "redirect:/login";
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders";
    }

    // ===================== SIMPLE REPORT =====================
    @GetMapping("/reports")
    public String viewReports(HttpSession session, Model model) {
        if (getLoggedAdmin(session) == null) return "redirect:/login";
        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("totalMedicines", medicineService.countMedicines());
        model.addAttribute("totalOrders", orderService.countOrders());
        model.addAttribute("totalRevenue", orderService.getTotalRevenue());
        return "admin/reports";
    }
}