package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.LoginUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Client;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.model.UtilisateurRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImpTest {

    @Mock
    private UtilisateurRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthServiceImp authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignupSuccess() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setNom("John");
        registerUserDto.setPrenom("Doe");
        registerUserDto.setEmail("john.doe@example.com");
        registerUserDto.setPassword("password123");

        Role clientRole = new Role();
        clientRole.setName(RoleEnum.CLIENT);
        when(roleRepository.findByName(RoleEnum.CLIENT)).thenReturn(Optional.of(clientRole));

        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("encodedPassword");

        Utilisateur savedUser = new Client();
        savedUser.setEmail(registerUserDto.getEmail());
        savedUser.setRole(clientRole);
        when(userRepository.save(any(Utilisateur.class))).thenReturn(savedUser);

        Utilisateur result = authService.signup(registerUserDto);

        assertNotNull(result, "Le résultat ne doit pas être nul");
        assertEquals("john.doe@example.com", result.getEmail(), "L'email de l'utilisateur ne correspond pas");
        assertEquals(clientRole, result.getRole(), "Le rôle de l'utilisateur ne correspond pas");
    }



    @Test
    void testSignupRoleNotFound() {
        RegisterUserDto registerDto = new RegisterUserDto();
        registerDto.setNom("John");
        registerDto.setPrenom("Doe");

        when(roleRepository.findByName(RoleEnum.CLIENT)).thenReturn(Optional.empty());

        Utilisateur result = authService.signup(registerDto);

        assertNull(result);
    }

    @Test
    void testAuthenticateSuccess() {
        LoginUserDto loginDto = new LoginUserDto("user@example.com", "password123");
        loginDto.setEmail("johndoe@example.com");
        loginDto.setPassword("password");

        Utilisateur user = new Utilisateur();
        user.setEmail("johndoe@example.com");

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));

        Utilisateur result = authService.authenticate(loginDto);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void testInitiatePasswordResetSuccess() {
        String email = "test@example.com";
        Utilisateur user = new Utilisateur();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        authService.initiatePasswordReset(email);

        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1)).sendPasswordResetEmail(eq(email), anyString());
    }

    @Test
    void testResetPassword_TokenExpired() {
        String token = "expired-token";
        String newPassword = "newPassword123";
        Utilisateur user = new Utilisateur();
        user.setResetToken(token);
        user.setResetTokenExpiration(LocalDateTime.now().minusHours(1));

        when(userRepository.findByResetToken(token)).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> {
            authService.resetPassword(token, newPassword);
        });
    }
}

