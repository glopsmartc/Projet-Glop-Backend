package com.gestioncontrats.gestioncontrats.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private static final String SECRET_KEY = "8053dd0a9cf773233ca096263caba301edb9f2a1dd60265f2f4c461b25d0bedd";
    private String validToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validToken = Jwts.builder()
                .setSubject("testuser")
                .claim("roles", Collections.singletonList("ROLE_USER"))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void testDoFilterInternal_withValidToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication, "L'authentification doit être définie pour un token valide");
        assertEquals("testuser", authentication.getName(), "Le nom d'utilisateur doit correspondre");
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")), "Le rôle ROLE_USER doit être présent");

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_withInvalidToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "L'authentification doit être null pour un token invalide");

        verify(filterChain).doFilter(request, response);
    }


    @Test
    void testDoFilterInternal_withoutToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(),
                "L'authentification doit être null en l'absence de token");

        verify(filterChain).doFilter(request, response);
    }


    @Test
    void testExtractToken_withValidHeader() {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        String token = jwtAuthenticationFilter.extractToken(request);

        assertEquals(validToken, token, "Le token doit être extrait correctement");
    }


    @Test
    void testExtractToken_withInvalidHeader() {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        String token = jwtAuthenticationFilter.extractToken(request);

        assertNull(token, "Le token doit être null si l'en-tête est invalide");
    }


    @Test
    void testExtractToken_withoutHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        String token = jwtAuthenticationFilter.extractToken(request);

        assertNull(token, "Le token doit être null si l'en-tête est absent");
    }


    @Test
    void testValidateToken_withValidToken() {
        boolean isValid = jwtAuthenticationFilter.validateToken(validToken);

        assertTrue(isValid, "Un token valide doit être reconnu comme tel");
    }


    @Test
    void testValidateToken_withInvalidToken() {
        boolean isValid = jwtAuthenticationFilter.validateToken("invalidToken");

        assertFalse(isValid, "Un token invalide doit être rejeté");
    }
}