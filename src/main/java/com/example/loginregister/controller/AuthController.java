package com.example.loginregister.controller;

import com.example.loginregister.model.User;
import com.example.loginregister.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("userId") != null) {
            return "redirect:/home";
        }
        return "redirect:/login";
    }

    /* ------------------------- REGISTER ------------------------- */

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           @RequestParam(name = "confirmPassword", required = false) String confirmPassword,
                           RedirectAttributes redirectAttributes) {

        if (user.getPassword() != null && !user.getPassword().isEmpty()
                && !user.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("password", "password.mismatch", "Passwords do not match");
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()
                && userService.emailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "This email is already registered");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.register(user);
        redirectAttributes.addFlashAttribute("successMessage",
                "Registration successful! Please log in.");
        return "redirect:/login";
    }

    /* ------------------------- LOGIN ------------------------- */

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(name = "email", required = false) String email,
                        @RequestParam(name = "password", required = false) String password,
                        HttpSession session,
                        Model model) {

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("error", "Please enter both email and password");
            model.addAttribute("email", email);
            return "login";
        }

        User user = userService.login(email, password);
        if (user == null) {
            model.addAttribute("error", "Invalid email or password");
            model.addAttribute("email", email);
            return "login";
        }

        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        return "redirect:/home";
    }

    /* ------------------------- LOGOUT ------------------------- */

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}