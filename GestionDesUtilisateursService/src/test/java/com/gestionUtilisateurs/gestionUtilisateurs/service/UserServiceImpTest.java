package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.model.UtilisateurRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImpTest {

    @Mock
    private UtilisateurRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImp userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateConseillerSuccess() {
        RegisterUserDto registerDto = new RegisterUserDto();
        registerDto.setNom("John");
        registerDto.setPrenom("Doe");
        registerDto.setEmail("johndoe@example.com");
        registerDto.setPassword("password");

        Role conseillerRole = new Role();
        conseillerRole.setName(RoleEnum.CONSEILLER);

        Utilisateur savedUser = new Utilisateur();
        savedUser.setRole(conseillerRole); // Attribuer le rôle au mock d'utilisateur

        when(roleRepository.findByName(RoleEnum.CONSEILLER)).thenReturn(Optional.of(conseillerRole));
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(savedUser); // Retourner l'utilisateur avec le rôle

        Utilisateur result = userService.createConseiller(registerDto);

        assertNotNull(result);
        assertEquals(conseillerRole, result.getRole(), "Le rôle attribué ne correspond pas");
        verify(userRepository, times(1)).save(any(Utilisateur.class));
    }

}
