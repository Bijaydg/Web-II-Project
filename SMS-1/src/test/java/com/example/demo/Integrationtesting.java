package com.example.demo;

import com.example.demo.entity.Intro;
import com.example.demo.repository.IntroRepository;
import com.example.demo.service.IntroService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.validation.ConstraintViolationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Sms1Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class Integrationtesting {

    @Autowired
    private IntroRepository introRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private IntroService introService;

   

    @Test
    @WithMockUser(username = "shyam", roles = { "ADMIN" })
    public void testAddUser_Success() {
        Intro user = new Intro();

        Intro savedUser = introService.addUser(user);
        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john@example.com", savedUser.getEmail());
        assertNotEquals("password", savedUser.getPassword()); // Check that password is encoded
    }

    @Test
    @WithMockUser(username = "shyam", roles = { "ADMIN" })
    public void testAddUser_ValidationFailure() {
        Intro user = new Intro();

        assertThrows(ConstraintViolationException.class, () -> {
            introService.addUser(user);
        });
    }

    @Test
    @WithMockUser(username = "shyam", roles = { "ADMIN" })
    public void testGetAllUsers() {
        Intro user1 = new Intro();
        Intro user2 = new Intro();
        introService.addUser(user1);
        introService.addUser(user2);

        var users = introService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    @WithMockUser(username = "shyam", roles = { "ADMIN" })
    public void testGetUserById_UserExists() {
        Intro user = new Intro();
        Intro savedUser = introService.addUser(user);

        Optional<Intro> foundUser = introService.getUserById(savedUser.getID());
        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
    }

    @Test
    @WithMockUser(username = "shyam", roles = { "ADMIN" })
    public void testGetUserById_UserDoesNotExist() {
        Optional<Intro> foundUser = introService.getUserById(999); // Assuming this ID doesn't exist
        assertFalse(foundUser.isPresent());
    }

    @Test
    @WithMockUser(username = "shyam", roles = { "ADMIN" })
    public void testUpdateUser_Success() {
        Intro user = new Intro();
        Intro savedUser = introService.addUser(user);
        
        Intro updatedUser = new Intro();

        Intro result = introService.updateUser(savedUser.getID(), updatedUser);
        assertEquals("John Smith", result.getName());
        assertEquals("johnsmith@example.com", result.getEmail());
        assertNotEquals("newpassword", result.getPassword()); // Check that password is encoded
    }

    @Test
    @WithMockUser(username = "shyam", roles = { "ADMIN" })
    public void testUpdateUser_UserNotFound() {
        Intro user = new Intro();
        
        assertThrows(IllegalArgumentException.class, () -> {
            introService.updateUser(999, user); // Assuming this ID doesn't exist
        });
    }

    @Test
    
    @WithMockUser(username = "shyam", roles = { "ADMIN" })
    public void testDeleteUser_Success() {
        Intro user = new Intro();
        Intro savedUser = introService.addUser(user);

        introService.deleteUser(savedUser.getID());
        Optional<Intro> foundUser = introService.getUserById(savedUser.getID());
        assertFalse(foundUser.isPresent());
    }
}