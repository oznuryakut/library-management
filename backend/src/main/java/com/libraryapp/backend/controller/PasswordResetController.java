package com.libraryapp.backend.controller;

import com.libraryapp.backend.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/password")
@Tag(name = "Auth")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Şifre sıfırlama kodu gönder")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> body) {
        passwordResetService.sendResetCode(body.get("email"));
        return ResponseEntity.ok(Map.of("message", "Sıfırlama kodu email adresinize gönderildi"));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Şifreyi sıfırla")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        passwordResetService.resetPassword(
            body.get("email"),
            body.get("code"),
            body.get("newPassword")
        );
        return ResponseEntity.ok(Map.of("message", "Şifreniz başarıyla güncellendi"));
    }
}