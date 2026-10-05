package com.example.loginregister.service;

import com.example.loginregister.model.User;
import com.example.loginregister.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** True if an account with this email already exists. */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(normalizeEmail(email));
    }

    /** Hashes the password and saves the new user. */
    public User register(User user) {
        user.setUsername(user.getUsername().trim());
        user.setEmail(normalizeEmail(user.getEmail()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Returns the user when the credentials are correct,
     * or null when the email is unknown or the password is wrong.
     */
    public User login(String email, String rawPassword) {
        Optional<User> found = userRepository.findByEmail(normalizeEmail(email));
        if (found.isEmpty()) {
            return null;
        }
        User user = found.get();
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return null;
        }
        return user;
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}