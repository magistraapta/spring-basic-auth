package com.basicauth.basicauth.auth.service;
import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.basicauth.basicauth.auth.dto.LoginRequest;
import com.basicauth.basicauth.auth.entity.User;
import com.basicauth.basicauth.auth.repository.AuthRepository;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return authRepository.save(user);
    }

    public Optional<User> login(User user) {
        Optional<User> existingUser = authRepository.findByUsername(user.getUsername());
        if (existingUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return existingUser;
    }
    
}
