package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Intro;
import com.example.demo.repository.IntroRepository;
import com.example.demo.service.IntroService;

@Controller
public class SMSController {
	
	@Autowired
	private IntroService introService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private IntroRepository introRepository;
	
	 @GetMapping("/") // This maps to the root URL
	    public String home(Model model) {
	        // your home logic
	        return "home";
	    }
	 

	    @GetMapping("/users") // This maps to /users, resolving the conflict
	    public String listUser(Model model) {
	        // your user list logic
	        return "userList";
	    }
	    @GetMapping("/home")
	    public String home() {
	    	
	        return "home";
	    }
	    
	    
	

	
//	@GetMapping("/home")
//	public String listUser(Model model) {
//	    List<Intro> users = introService.getAllUsers();  // Use the service layer to retrieve users
//	    model.addAttribute("students", users);
//	    return "home";  // Return the view for login (assumed login.html is in /templates)
//	}
//
//
////    private List<Intro> userList = new ArrayList<>();
//   // private int ID = 1;

    // Home page - Login
//    @GetMapping("/")
//    public String home(Model model) {
//        model.addAttribute("user", new Intro());
//        return "home";  // Returns the home page (login page)
//    }

    // Handle login form submission
//    @PostMapping("/home")
//    public String addUser(Intro user, Model model) {
//        System.out.println("User Name: " + user.getName());
//        System.out.println("User Password: " + user.getPassword());
//
//        if ("Passa".equals(user.getName()) && "123456".equals(user.getPassword())) {
//            return "redirect:/records";  // Redirect to records if login is successful
//        } else {
//            model.addAttribute("user", user);
//            model.addAttribute("Error", "Invalid username or password.");
//            return "home";  // Return to home with error message if login fails
//        }
//    }

    // About Us page
    @GetMapping("/aboutus")
    public String showForm(Model model) {
        model.addAttribute("user", new Intro());
        return "";  // Displays the about us form
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
        return "redirect:/records";  // Redirect to records page after signup
    }
    
    @GetMapping("/add")
	public String addStudents(Model model) {
		model.addAttribute("Welcome",new Intro());
		return "/add";

	}
    @PostMapping("/add")
    public String addStudentintro(@ModelAttribute Intro user) {
        introService.addUser(user);  // Use the correct service method, e.g., addUser
        return "redirect:/records";  // Redirect to the user list or the appropriate page after signup
    }


    // Display the records page with registered users


//    // GET request to show the records page with the list of users
    @GetMapping("/records")
    public String showRecord(Model model) {
    	
        model.addAttribute("userList", introService.getAllUsers());  // Pass the list of registered users to the records page
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
    
    @GetMapping("/update/{id}")
    public String editUserForm(@PathVariable int id, Model model) {
        Optional<Intro> optionalIntro = introService.getUserById(id);  // Use the service layer
        if (optionalIntro.isPresent()) {
            Intro user = optionalIntro.get();
            user.setPassword(null);  // Clear password for security reasons before sending to the form
            model.addAttribute("user", user);
            return "update";  // Return the view for editing the user
        }
        return "redirect:/records";  // Redirect to user list if ID not found
    }



 // Update user form submission (Update operation)
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable int id, @ModelAttribute Intro user) {
        Optional<Intro> existingUser = introService.getUserById(id);
        
        if (existingUser.isPresent()) {
            Intro userToUpdate = existingUser.get();
            userToUpdate.setName(user.getName());
            userToUpdate.setUsername(user.getUsername());

            
			// Only encode and update the password if it was changed
            if (!user.getPassword().isEmpty() && 
                user.getPassword().equals(user.getConfirmPassword()) &&
                !passwordEncoder.matches(user.getPassword(), userToUpdate.getPassword())) {
                
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                userToUpdate.setPassword(encodedPassword);
            }

            introService.updateUser(userToUpdate); // Save the updated user
        }

        return "redirect:/users"; // Redirect to the user list after update
    }


    

    // Delete a user by ID (Delete operation)
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id, Model model) {
        Optional<Intro> user = introService.getUserById(id);
        if (user.isPresent()) {
            introService.deleteUser(id);
            model.addAttribute("message", "User Deleted Successfully");
        } else {
            model.addAttribute("errorMessage", "User Not Found");
        }

        return "redirect:/records"; // Redirect to the user list
    }
}

//    Show form for creating a new user
//    @GetMapping("/create")
//    public String showCreateForm(Model model) {
//        model.addAttribute("user", new Intro());  // Provide a new user object for the form
//       return "user-create";  // This should be the form page for creating a new user
//   }
//
//    @PostMapping("/create")
//    public String createUser(@ModelAttribute Intro user) {
//       // Ensure password is not empty and confirm password matches
//       if (!user.getPassword().isEmpty() && user.getPassword().equals(user.getConfirmPassword())) {
//           user.setPassword(passwordEncoder.encode(user.getPassword()));  // Use the autowired passwordEncoder
//           introService.addUser(user);  // Save the new user
//       } else {
//          // Handle the case when the password doesn't match or is empty (Optional: add error handling)
//           return "redirect:/"; // Optionally redirect to signup with an error message
//      }
//      
//      return "redirect:/records";  // Redirect to user list after successful creation
//   }
//



