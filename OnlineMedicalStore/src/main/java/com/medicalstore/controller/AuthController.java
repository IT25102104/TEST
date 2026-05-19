package com.medicalstore.controller;

import com.medicalstore.entity.User;
import com.medicalstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;


@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        // Basic validation
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Email already exists!");
            return "register";
        }
        user.setRole("CUSTOMER"); // default role
        userService.registerUser(user);
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,    // Add this parameter
                            Model model) {
        if (userService.authenticate(email, password)) {
            session.setAttribute("userEmail", email);   // Store email in session
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }
}