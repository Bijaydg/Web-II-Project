package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Intro;
import com.example.demo.repository.IntroRepository;
import com.example.demo.service.IntroService;

@Controller
public class SMSController {
	
	@Autowired
	private IntroService introService;

//    private List<Intro> userList = new ArrayList<>();
   // private int ID = 1;

    // Home page - Login
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("user", new Intro());
        return "home";  // Returns the home page (login page)
    }

    // Handle login form submission
    @PostMapping("/home")
    public String addUser(Intro user, Model model) {
        System.out.println("User Name: " + user.getName());
        System.out.println("User Password: " + user.getPassword());

        if ("Passa".equals(user.getName()) && "123456".equals(user.getPassword())) {
            return "redirect:/records";  // Redirect to records if login is successful
        } else {
            model.addAttribute("user", user);
            model.addAttribute("Error", "Invalid username or password.");
            return "home";  // Return to home with error message if login fails
        }
    }

    // About Us page
    @GetMapping("/aboutus")
    public String showForm(Model model) {
        model.addAttribute("user", new Intro());
        return "aboutus";  // Displays the about us form
    }

    // Handle form submission on about us page
    @PostMapping("/aboutus")
    public String handleFormSubmission(Intro user, Model model) {
        model.addAttribute("user", new Intro());  // Reset form with a new empty user object
        return "records";  // Redirect to records page (or wherever intended)
    }

    // Display the signup form
    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new Intro());  // Add an empty user object for the signup form
        return "signup";  // Returns the signup page
    }

    // Handle user registration (signup)
    @PostMapping("/signup")
    public String handleSignup(Intro user, Model model) {
        //user.setID(ID++);
       // userList.add(user);  // Add the user to the list
    	introService.addUser(user);
       // model.addAttribute("registeredUser", user);  // Add the registered user info to the model
        return "redirect:/";  // Redirect to records page after signup
    }

    // Display the records page with registered users


//    // GET request to show the records page with the list of users
    @GetMapping("/records")
    public String showRecord(Model model, Object userList) {
        model.addAttribute("userList", userList);  // Pass the list of registered users to the records page
        return "records";  // Displays the records page with the list of users
    }

    // POST request to handle form submission for adding or updating users
    @PostMapping("/records")
    public String postRecord(Intro user, Model model, Object userList) {
        // Add the user to the list and assign an ID if it's a new user
        if (user.getID() == 0) {
           // user.setID(ID++);  // Auto-increment the ID for new users
          //  userList.add(user);  // Add the new user to the userList
        }

        // Pass the updated user list to the model
        model.addAttribute("userList", userList);  // Update the list of users

        return "records";  // Stay on the records page after the submission
    }
}
