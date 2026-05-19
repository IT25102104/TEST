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
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // Helper – get current user from session
    private User getCurrentUser(HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            return userService.findByEmail(email).orElse(null);
        }
        return null;
    }

    // 1. Checkout (place order) – triggered by a button in cart page
    @PostMapping("/checkout")
    public String checkout(HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";
        Order order = orderService.placeOrder(user);
        return "redirect:/orders/confirmation/" + order.getId();
    }

    // 2. Order confirmation page
    @GetMapping("/confirmation/{orderId}")
    public String confirmation(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order-confirmation";
    }

    // 3. Order history for the logged‑in user
    @GetMapping
    public String myOrders(HttpSession session, Model model) {
        User user = getCurrentUser(session);
        if (user == null) return "redirect:/login";
        List<Order> orders = orderService.getOrdersForUser(user);
        model.addAttribute("orders", orders);
        return "order-history";
    }

    // 4. Admin: view all orders (we'll reuse the same page for admin later)
    //    For now, just a user‑specific history.
}