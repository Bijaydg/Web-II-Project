package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Intro;
import com.example.demo.service.IntroService;

import java.util.Optional;

@Controller
public class SMSController {

    @Autowired
    private IntroService introService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("user", new Intro());
        return "home";
    }

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new Intro());
        return "signup";
    }

    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute Intro user) {
        introService.addUser(user);
        return "redirect:/records";
    }

    @GetMapping("/records")
    public String showRecord(Model model) {
        model.addAttribute("userList", introService.getAllUsers());
        return "records";
    }

    @GetMapping("/update/{id}")
    public String editUserForm(@PathVariable int id, Model model) {
        Optional<Intro> optionalIntro = introService.getUserById(id);
        if (optionalIntro.isPresent()) {
            Intro user = optionalIntro.get();
            user.setPassword(null); // Clear password for security reasons
            model.addAttribute("user", user);
            return "update";
        }
        return "redirect:/records";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable int id, @ModelAttribute Intro user, Model model) {
        try {
            introService.updateUser(id, user);
            return "redirect:/records";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "update"; // Return to update form on error
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        introService.deleteUser(id);
        return "redirect:/records";
    }
}
