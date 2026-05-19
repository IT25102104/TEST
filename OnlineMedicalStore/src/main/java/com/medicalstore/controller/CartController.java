package com.medicalstore.controller;

import com.medicalstore.entity.*;
import com.medicalstore.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;   // Assuming you have UserService

    // Helper: get the currently logged‑in user (simplified – we'll improve later)
    private User getCurrentUser(HttpSession session) {
        // For now, we assume user email is stored in session after login
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            return userService.findByEmail(email).orElse(null);
        }
        return null;
    }

    // View cart
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        User user = getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        Cart cart = cartService.getOrCreateCart(user);
        double total = cartService.getCartTotal(cart);
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "cart";
    }

    // Add item to cart
    @PostMapping("/add/{medicineId}")
    public String addToCart(@PathVariable Long medicineId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        cartService.addToCart(user, medicineId, quantity);
        return "redirect:/cart";
    }

    // Update quantity
    @PostMapping("/update/{cartItemId}")
    public String updateQuantity(@PathVariable Long cartItemId,
                                 @RequestParam int quantity,
                                 HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        cartService.updateCartItem(user, cartItemId, quantity);
        return "redirect:/cart";
    }

    // Remove item
    @GetMapping("/remove/{cartItemId}")
    public String removeItem(@PathVariable Long cartItemId, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        cartService.removeCartItem(user, cartItemId);
        return "redirect:/cart";
    }

    // Clear cart (optional)
    @GetMapping("/clear")
    public String clearCart(HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        cartService.clearCart(user);
        return "redirect:/cart";
    }
}