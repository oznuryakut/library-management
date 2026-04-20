package com.libraryapp.backend.service;

import com.libraryapp.backend.dto.request.LoginRequest;
import com.libraryapp.backend.dto.request.RegisterRequest;
import com.libraryapp.backend.dto.response.AuthResponse;
import com.libraryapp.backend.model.Role;
import com.libraryapp.backend.model.User;
import com.libraryapp.backend.repository.UserRepository;
import com.libraryapp.backend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new IllegalArgumentException("Username already taken");
        if (userRepository.existsByEmail(request.getEmail()))
            throw new IllegalArgumentException("Email already in use");

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user), user.getUsername(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        return new AuthResponse(jwtService.generateToken(user), user.getUsername(), user.getRole().name());
    }
}