package com.gestionUtilisateurs.gestionUtilisateurs.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MailConfigTest {

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    void testJavaMailSenderBean() {
        assertNotNull(javaMailSender);
    }
}
