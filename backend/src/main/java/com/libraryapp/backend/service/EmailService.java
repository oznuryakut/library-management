package com.libraryapp.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("oznuryakut35555@gmail.com");
        message.setTo(to);
        message.setSubject("LibraryApp - Şifre Sıfırlama Kodu");
        message.setText(
            "Merhaba,\n\n" +
            "Şifre sıfırlama kodunuz: " + code + "\n\n" +
            "Bu kod 15 dakika geçerlidir.\n\n" +
            "Eğer bu isteği siz yapmadıysanız, bu emaili görmezden gelebilirsiniz.\n\n" +
            "LibraryApp"
        );
        mailSender.send(message);
    }
}