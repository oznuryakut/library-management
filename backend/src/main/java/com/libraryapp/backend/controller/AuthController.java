// controller/AuthController.java
package com.libraryapp.backend.controller;

import com.libraryapp.backend.dto.request.*;
import com.libraryapp.backend.dto.response.AuthResponse;
import com.libraryapp.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Register ve login işlemleri")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Yeni kullanıcı kaydı")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Giriş yap, JWT token al")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}