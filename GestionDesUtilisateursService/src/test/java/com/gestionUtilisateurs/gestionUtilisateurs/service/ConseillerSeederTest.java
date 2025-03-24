package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.bootstrap.ConseillerSeeder;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

class ConseillerSeederTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UtilisateurRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ConseillerSeeder conseillerSeeder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateConseiller_RoleNotFound() {

        when(roleRepository.findByName(RoleEnum.CONSEILLER)).thenReturn(Optional.empty());


        conseillerSeeder.createConseiller();

        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateConseiller_UserAlreadyExists() {
        Role mockRole = new Role();
        mockRole.setName(RoleEnum.CONSEILLER);
        when(roleRepository.findByName(RoleEnum.CONSEILLER)).thenReturn(Optional.of(mockRole));

        when(userRepository.findByEmail("super.conseiller@email.com")).thenReturn(Optional.of(new Utilisateur()));

        conseillerSeeder.createConseiller();

        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateConseiller_Success() {
        Role mockRole = new Role();
        mockRole.setName(RoleEnum.CONSEILLER);
        when(roleRepository.findByName(RoleEnum.CONSEILLER)).thenReturn(Optional.of(mockRole));

        when(userRepository.findByEmail("super.conseiller@email.com")).thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");

        conseillerSeeder.createConseiller();

        verify(userRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void testOnApplicationEvent() {
        ContextRefreshedEvent event = mock(ContextRefreshedEvent.class);

        conseillerSeeder.onApplicationEvent(event);

        verify(roleRepository, atLeastOnce()).findByName(RoleEnum.CONSEILLER);
    }
}
