package com.example.demo.service;

import jakarta.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.demo.entity.Intro;
import com.example.demo.repository.IntroRepository;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class IntroService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IntroRepository introRepository;

    // Get all users
    public List<Intro> getAllUsers() {
        return introRepository.findAll();
    }

    // Get user by ID
    public Optional<Intro> getUserById(int id) {
        return introRepository.findById(id);
    }

    // Add a new user
    public Intro addUser(@Valid Intro user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return introRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Validation failed: " + e.getMessage(), e);
        }
    }

    // Update an existing user
    public Intro updateUser(int id, @Valid Intro user) {
        Optional<Intro> existingUserOptional = introRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            Intro existingUser = existingUserOptional.get();
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return introRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // Delete a user
    public void deleteUser(int id) {
        introRepository.deleteById(id);
    }
}
