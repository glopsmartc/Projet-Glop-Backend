package com.gestioncontrats.gestioncontrats;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GestioncontratsApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodTest() {
		GestioncontratsApplication.main(new String[]{});
	}
}
