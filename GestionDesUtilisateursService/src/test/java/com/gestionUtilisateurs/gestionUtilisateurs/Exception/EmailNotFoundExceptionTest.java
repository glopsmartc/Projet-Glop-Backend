package com.gestionUtilisateurs.gestionUtilisateurs.Exception;

import com.gestionUtilisateurs.gestionUtilisateurs.exception.EmailNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailNotFoundExceptionTest {

    @Test
    void testEmailNotFoundException() {
        String errorMessage = "Email not found";
        EmailNotFoundException exception = new EmailNotFoundException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}