package com.example.demo;

import com.example.demo.controller.SMSController;
import com.example.demo.entity.Intro;
import com.example.demo.service.IntroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SMSunittest {

    @InjectMocks
    private SMSController smsController;

    @Mock
    private IntroService introService;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testHandleSignup() {
        Intro user = new Intro();
        smsController.handleSignup(user);
        verify(introService).addUser(user);
    }

    @Test
    public void testShowRecord() {
        List<Intro> users = new ArrayList<>();
        when(introService.getAllUsers()).thenReturn(users);

        String viewName = smsController.showRecord(model);
        assertEquals("records", viewName);
        verify(model).addAttribute("userList", users);
    }

    @Test
    public void testEditUserForm_UserExists() {
        Intro user = new Intro();
        user.setID(1); // Assuming there's a setId method
        when(introService.getUserById(1)).thenReturn(Optional.of(user));

        String viewName = smsController.editUserForm(1, model);
        assertEquals("update", viewName);
        verify(model).addAttribute("user", user);
    }

    @Test
    public void testEditUserForm_UserDoesNotExist() {
        when(introService.getUserById(1)).thenReturn(Optional.empty());

        String viewName = smsController.editUserForm(1, model);
        assertEquals("redirect:/records", viewName);
    }

    @Test
    public void testUpdateUser_Success() {
        Intro user = new Intro();
        smsController.updateUser(1, user, model);
        verify(introService).updateUser(1, user);
    }

    @Test
    public void testUpdateUser_Error() {
        Intro user = new Intro();
        doThrow(new IllegalArgumentException("Error")).when(introService).updateUser(1, user);

        String viewName = smsController.updateUser(1, user, model);
        assertEquals("update", viewName);
        verify(model).addAttribute("errorMessage", "Error");
    }

    @Test
    public void testDeleteUser() {
        smsController.deleteUser(1);
        verify(introService).deleteUser(1);
    }
}