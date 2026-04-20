package com.libraryapp.backend.service;

import com.libraryapp.backend.model.PasswordResetToken;
import com.libraryapp.backend.repository.PasswordResetTokenRepository;
import com.libraryapp.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(UserRepository userRepository,
                                 PasswordResetTokenRepository tokenRepository,
                                 EmailService emailService,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void sendResetCode(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Bu email ile kayıtlı kullanıcı bulunamadı"));

        tokenRepository.deleteByUserId(user.getId());

        String code = String.format("%06d", new Random().nextInt(999999));

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(code);
        token.setUser(user);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        token.setUsed(false);
        tokenRepository.save(token);

        emailService.sendPasswordResetEmail(email, code);
    }

    @Transactional
    public void resetPassword(String email, String code, String newPassword) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı"));

        var token = tokenRepository.findByToken(code)
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz kod"));

        if (token.isUsed())
            throw new IllegalArgumentException("Bu kod zaten kullanıldı");

        if (token.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Kodun süresi dolmuş");

        if (!token.getUser().getId().equals(user.getId()))
            throw new IllegalArgumentException("Geçersiz kod");

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        token.setUsed(true);
        tokenRepository.save(token);
    }
}