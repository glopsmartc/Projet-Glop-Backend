package com.gestionUtilisateurs.gestionUtilisateurs.service;


import com.gestionUtilisateurs.gestionUtilisateurs.email.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendPasswordResetEmail_Success() {
        String toEmail = "test@example.com";
        String resetToken = "reset-token";

        emailService.sendPasswordResetEmail(toEmail, resetToken);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendPasswordResetEmail_Failure() {
        String toEmail = "test@example.com";
        String resetToken = "reset-token";

        doThrow(new MailException("Failed to send email") {}).when(mailSender).send(any(SimpleMailMessage.class));

        assertThrows(MailException.class, () -> {
            emailService.sendPasswordResetEmail(toEmail, resetToken);
        });
    }
}
