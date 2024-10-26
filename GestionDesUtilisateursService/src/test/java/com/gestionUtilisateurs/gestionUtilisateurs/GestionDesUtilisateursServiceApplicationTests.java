package com.gestionUtilisateurs.gestionUtilisateurs;

import com.gestionUtilisateurs.gestionUtilisateurs.config.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class GestionDesUtilisateursServiceApplicationTests {

	@MockBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Test
	void contextLoads() {
	}

}
