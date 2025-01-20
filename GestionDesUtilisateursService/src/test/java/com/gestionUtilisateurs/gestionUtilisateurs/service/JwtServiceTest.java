package com.gestionUtilisateurs.gestionUtilisateurs.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        setPrivateField(jwtService, "secretKey", "mySecretKey12345678901234567890123456789012");
        setPrivateField(jwtService, "jwtExpiration", 3600000L);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testExtractUsername() {
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    void testExtractRoles() {
        UserDetails user = new User("user@example.com", "password", List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER")));

        String token = jwtService.generateToken(user);

        List<String> roles = jwtService.extractRoles(token);

        assertNotNull(roles);
        assertTrue(roles.contains("ROLE_USER"));
    }

    @Test
    void testGenerateToken() {
        UserDetails user = new User("user@example.com", "password", List.of());

        String token = jwtService.generateToken(user);

        assertNotNull(token);
    }

    @Test
    void testGenerateTokenWithExtraClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customClaim", "customValue");

        UserDetails user = new User("user@example.com", "password", List.of());

        String token = jwtService.generateToken(extraClaims, user);

        assertNotNull(token);

        Claims claims = jwtService.extractAllClaims(token);

        assertEquals("customValue", claims.get("customClaim"));
    }

    @Test
    void testGetExpirationTime() {
        assertEquals(3600000, jwtService.getExpirationTime());
    }

    @Test
    void testIsTokenValid() {
        UserDetails user = new User("user@example.com", "password", List.of());

        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.isTokenValid(token, user);

        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() {
        UserDetails user = new User("user@example.com", "password", List.of());

        String token = jwtService.generateToken(user);

        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void testExtractExpiration() {
        UserDetails user = new User("user@example.com", "password", List.of());

        String token = jwtService.generateToken(user);

        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
    }

    @Test
    void testExtractAllClaims() {
        UserDetails user = new User("user@example.com", "password", List.of());

        String token = jwtService.generateToken(user);

        Claims claims = jwtService.extractAllClaims(token);

        assertNotNull(claims);
        assertEquals("user@example.com", claims.getSubject());
    }

    @Test
    void testGetSignInKey() {
        Key key = jwtService.getSignInKey();

        assertNotNull(key);
    }
}