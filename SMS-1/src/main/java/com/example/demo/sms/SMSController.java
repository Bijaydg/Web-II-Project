package com.example.demo.sms;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import entity.Intro;
import org.springframework.web.bind.annotation.RequestBody;


@Controller




public class SMSController {
	private List<Intro> userList = new ArrayList<>();
	private int ID = 1;
	
	
@GetMapping ("/home") 
public String home(Model model) {
	model.addAttribute("user", new Intro());
	return "home";
}
@PostMapping("/home")
public String addUser( Intro user, Model model) { // Corrected
    System.out.println(user.getName());
    System.out.println(user.getPassword());
	if ("Passa".equals(user.getName()) && "123456".equals(user.getPassword())) { // Corrected
        return "redirect:/aboutus";
    } else {
    	model.addAttribute("user",user);
        model.addAttribute("Error", "Invalid username or password.");
        return "home";
    }
	
}
@GetMapping("/aboutus")
public String showForm(Model model) {
   
    model.addAttribute("user", new Intro());
    return "aboutus"; 
}
@PostMapping("/aboutus") 
public String handleFormSubmission(Intro user, Model model) {
  
//    System.out.println("User Name: " + user.getName());
//    System.out.println("User Email: " + user.getEmail());

   
    model.addAttribute("user", new Intro());
    return "records"; 
}

@GetMapping("/records")
public String showRecord() {
	return "records";
}

@PostMapping("/records")
public String postMethodName(Intro user, Model model) {
    //TODO: process POST request
	System.out.println(user);
	model.addAttribute("user", user);
    return "records";
   
}


//@GetMapping("/signup.html")
//public String regishere (Intro user) {
//	return "signup";
//}
//	@PostMapping("/signup.html")
//	public String methodtwo(Intro user, Model model) {
//		user.setID(ID);
//		ID++;
//		smsHi.add(user);
//		return "register";
//	}
@GetMapping("/signup.html")
public String showSignupPage(Model model) {
    model.addAttribute("user", new Intro()); // Add an empty user object for the signup form
    return "signup";
}

// Handle user registration
@PostMapping("/signup.html")
public String handleSignup(Intro user, Model model) {
    user.setID(ID);
    ID++; // Auto-increment ID for each new user
//    userList = null;
	userList.add(user); // Add the user to the list (simulating a database)
    model.addAttribute("registeredUser", user); // Add the registered user info to the model
    return "records"; // Show registration success page
}

}


