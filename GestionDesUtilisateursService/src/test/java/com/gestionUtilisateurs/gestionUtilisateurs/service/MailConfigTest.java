package com.gestionUtilisateurs.gestionUtilisateurs.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MailConfigTest {

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void testJavaMailSenderBean() {
        assertNotNull(javaMailSender);
    }
}
