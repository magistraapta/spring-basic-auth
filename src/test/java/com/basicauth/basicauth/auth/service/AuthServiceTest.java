package com.basicauth.basicauth.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.basicauth.basicauth.auth.entity.User;
import com.basicauth.basicauth.auth.repository.AuthRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
    }

    @Test
    void registerUser_Success() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(authRepository.save(any(User.class))).thenReturn(user);
        
        // Act
        authService.register(user);
        
        // Assert
        verify(passwordEncoder).encode("password123");
        verify(authRepository).save(any(User.class));
    }

    @Test
    void loginUser_Success() {
        // Arrange
        User savedUser = new User();
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");

        when(authRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches(testUser.getPassword(), savedUser.getPassword())).thenReturn(true);

        // Act
        Optional<User> result = authService.login(testUser);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(savedUser.getUsername(), result.get().getUsername());
    }

    @Test
    void loginUser_UserNotFound() {
        // Arrange
        when(authRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            authService.login(testUser);
        });
    }

    @Test
    void loginUser_InvalidPassword() {
        // Arrange
        User savedUser = new User();
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");

        when(authRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(savedUser));
        when(passwordEncoder.matches(testUser.getPassword(), savedUser.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            authService.login(testUser);
        });
    }
} 