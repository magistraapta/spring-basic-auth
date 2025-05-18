package com.basicauth.basicauth.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.basicauth.basicauth.auth.entity.User;
import com.basicauth.basicauth.auth.repository.AuthRepository;
import com.basicauth.basicauth.auth.security.CustomUserDetailsService;
import com.basicauth.basicauth.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.basicauth.basicauth.auth.security.SecurityConfig;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthRepository authRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
    }

    @Test
    void register_Success() throws Exception {
        // Arrange
        when(authRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());
        when(authService.register(any(User.class))).thenReturn(testUser);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully!"));
    }

    @Test
    void register_UsernameTaken() throws Exception {
        // Arrange
        when(authRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already taken."));
    }

    @Test
    void login_Success() throws Exception {
        // Arrange
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            testUser.getUsername(), testUser.getPassword());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful!"));
    }
} 