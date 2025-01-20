package com.gestionUtilisateurs.gestionUtilisateurs.Exception;

import com.gestionUtilisateurs.gestionUtilisateurs.exception.AddFailedException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddFailedExceptionTest {

    @Test
    void testAddFailedException() {
        String errorMessage = "Failed to add user";
        AddFailedException exception = new AddFailedException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}