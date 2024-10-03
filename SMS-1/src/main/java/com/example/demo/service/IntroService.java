package com.example.demo.service;

import jakarta.validation.Valid;  // Corrected import
import org.hibernate.exception.ConstraintViolationException;  // Hibernate constraint violation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.demo.entity.Intro;
import com.example.demo.repository.IntroRepository;

import java.util.List;
import java.util.Optional;

@Service
@Validated  // Enables method-level validation for the service
public class IntroService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IntroRepository introRepository;

    // Method to get all users
    public List<Intro> getAllUsers() {
        return introRepository.findAll();
    }

    // Method to get a user by ID
    public Optional<Intro> getUserById(int id) {
        return introRepository.findById(id);
    }

    // Method to add a new user with validation
    public void addUser(@Valid Intro user) {
        try {
            // Encrypt the password before saving the user
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            introRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Validation failed: " + e.getMessage(), e);
        }
    }

    // Method to update an existing user with validation
    public void updateUser(@Valid Intro user) {
        try {
            // Encrypt the password if it's being updated
        	if(user.getPassword()!=null && !user.getPassword().isEmpty())
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            introRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Validation failed: " + e.getMessage(), e);
        }
    }
    

    // Method to delete a user by ID
    public void deleteUser(int id) {
        introRepository.deleteById(id);
    }
}
