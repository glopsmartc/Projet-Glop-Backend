package com.gestioncontrats.gestioncontrats;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GestioncontratsApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring application context loads successfully.
		// It ensures that the configuration, beans, and dependencies are correctly set up.
	}

	@Test
	void mainMethodTest() {
		// This test calls the main method to ensure it executes without throwing exceptions.
		// While this test doesn't contain assertions, it validates that the application can start.
		// Capturing the application startup and asserting no exceptions occur
		Assertions.assertDoesNotThrow(() ->
						GestioncontratsApplication.main(new String[]{}),
				"The main method should execute without throwing exceptions."
		);
	}
}
