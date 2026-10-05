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

    /* ------------------------------------------------------------------ */
    /*  Entry point                                                        */
    /* ------------------------------------------------------------------ */

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("userId") != null) {
            return "redirect:/home";
        }
        return "redirect:/register";
    }

    /* ------------------------------------------------------------------ */
    /*  REGISTRATION                                                       */
    /* ------------------------------------------------------------------ */

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

        // 1. Do the two passwords match?
        if (user.getPassword() != null && !user.getPassword().isEmpty()
                && !user.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("password", "password.mismatch", "Passwords do not match");
        }

        // 2. Is the email already taken?
        if (user.getEmail() != null && !user.getEmail().isBlank()
                && userService.emailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "This email is already registered");
        }

        // 3. Any error (including @NotBlank / @Email / @Size)? Show the form again.
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // 4. Hash the password and save.
        userService.register(user);

        redirectAttributes.addFlashAttribute("successMessage",
                "Registration successful! Please log in.");
        return "redirect:/login";
    }

    /* ------------------------------------------------------------------ */
    /*  LOGIN                                                              */
    /* ------------------------------------------------------------------ */

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

        // Create the authenticated session.
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());

        return "redirect:/home";
    }

    /* ------------------------------------------------------------------ */
    /*  HOME (protected)                                                   */
    /* ------------------------------------------------------------------ */

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";       // not logged in
        }
        model.addAttribute("username", session.getAttribute("username"));
        return "home";
    }

    /* ------------------------------------------------------------------ */
    /*  LOGOUT                                                             */
    /* ------------------------------------------------------------------ */

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}