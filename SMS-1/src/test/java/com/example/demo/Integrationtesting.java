package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.entity.Intro;
import com.example.demo.repository.IntroRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Sms1Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class Integrationtesting {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IntroRepository introRepository;

    @BeforeEach
    public void setUp() {
        Intro user = new Intro();
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password123");
        introRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        introRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetUserByIdSuccess() throws Exception {
        Intro user = introRepository.findAll().get(0);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getID()))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        Intro fetchedUser = (Intro) result.getModelAndView().getModel().get("user");
        assertNotNull(fetchedUser);
        assertEquals("John Doe", fetchedUser.getName());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetUserByIdFailureNotFound() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/99"))
                .andReturn();

        assertEquals(404, result.getResponse().getStatus());
        String errorMessage = result.getResponse().getErrorMessage();
        assertEquals("User not found", errorMessage);
    }
}