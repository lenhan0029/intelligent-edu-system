package com.edu.auth.service;

import com.edu.auth.entity.PasswordResetToken;
import com.edu.auth.entity.User;
import com.edu.auth.repository.PasswordResetTokenRepository;
import com.edu.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Value("${app.jwt.reset-password-expiration-ms:3600000}")
    private Long resetTokenDurationMs; // 1 hour

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    public void createPasswordResetTokenForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Delete existing tokens
        tokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setUser(user);
        myToken.setToken(token);
        myToken.setExpiryDate(Instant.now().plusMillis(resetTokenDurationMs));
        tokenRepository.save(myToken);

        // Send Email
        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Reset Password Request");
        message.setText("To reset your password, click the link below:\n" + resetUrl);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email. In production, this must be handled.");
        }
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Token has expired");
        }

        User user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }
}
