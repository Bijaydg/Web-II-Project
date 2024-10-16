package com.example.demo;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Intro;
import com.example.demo.repository.IntroRepository;
import com.example.demo.service.IntroService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class SMSunittest {

    @Mock
    private IntroRepository introRepository;

    @InjectMocks
    private IntroService introService;

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        List<Intro> userList = new ArrayList<>();
        Intro user = new Intro();
        user.setID(1);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        userList.add(user);

        when(introRepository.save(any(Intro.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(introRepository.findAll()).thenReturn(userList);
        when(introRepository.findById(1)).thenReturn(Optional.of(user));
        when(introRepository.findById(99)).thenReturn(Optional.empty());
        Mockito.doNothing().when(introRepository).deleteById(any());
    }

    @Test
    void testCreateUserSuccess() {
        Intro user = new Intro();
        user.setName("Jane Doe");
        user.setEmail("janedoe@example.com");
        user.setPassword("password123");

        Intro createdUser = introService.addUser(user);
        assertEquals("Jane Doe", createdUser.getName());
        assertThatNoException();
    }

    @Test
    void testCreateUserFailureValidation() {
        Intro user = new Intro();
        user.setName(null); // Invalid user

        Set<ConstraintViolation<Intro>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testUpdateUserSuccess() {
        Intro userToUpdate = new Intro();
        userToUpdate.setID(1);
        userToUpdate.setName("Updated Name");
        userToUpdate.setEmail("updated@example.com");
        userToUpdate.setPassword("newpassword123");

        Intro updatedUser = introService.updateUser(1, userToUpdate);
        assertEquals("Updated Name", updatedUser.getName());
    }

    @Test
    void testUpdateUserFailureNotFound() {
        Intro userToUpdate = new Intro();
        userToUpdate.setID(99);
        userToUpdate.setName("Nonexistent User");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            introService.updateUser(99, userToUpdate);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetAllUsersSuccess() {
        List<Intro> userList = introService.getAllUsers();
        assertEquals(1, userList.size());
        assertEquals("John Doe", userList.get(0).getName());
    }

    @Test
    void testGetAllUsersEmptyList() {
        when(introRepository.findAll()).thenReturn(new ArrayList<>());
        List<Intro> userList = introService.getAllUsers();
        assertTrue(userList.isEmpty());
    }

    @Test
    void testGetUserByIdSuccess() {
        Optional<Intro> user = introService.getUserById(1);
        assertTrue(user.isPresent());
        assertEquals("John Doe", user.get().getName());
    }

    @Test
    void testGetUserByIdFailureNotFound() {
        Optional<Intro> user = introService.getUserById(99);
        assertTrue(user.isEmpty());
    }

    @Test
    void testDeleteUserSuccess() {
        introService.deleteUser(1);
        assertThatNoException();
    }

    @Test
    void testDeleteUserFailureUserNotFound() {
        Mockito.doThrow(new RuntimeException("User not found")).when(introRepository).deleteById(99);
        assertThrows(RuntimeException.class, () -> introService.deleteUser(99));
    }
}
