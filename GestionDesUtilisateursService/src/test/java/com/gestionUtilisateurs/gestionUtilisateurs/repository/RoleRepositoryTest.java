package com.gestionUtilisateurs.gestionUtilisateurs.repository;

import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Role userRole = new Role();
        userRole.setName(RoleEnum.USER);
        userRole.setDescription("Role for regular users");
        roleRepository.save(userRole);

        Role adminRole = new Role();
        adminRole.setName(RoleEnum.MEDECIN);
        adminRole.setDescription("Role for medical staff");
        roleRepository.save(adminRole);
    }

    @Test
    void testFindByName() {
        Optional<Role> foundRole = roleRepository.findByName(RoleEnum.USER);

        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo(RoleEnum.USER);
        assertThat(foundRole.get().getDescription()).isEqualTo("Role for regular users");
    }

    @Test
    void testSaveRole() {
        Role newRole = new Role();
        newRole.setName(RoleEnum.CLIENT);
        newRole.setDescription("Role for clients");

        Role savedRole = roleRepository.save(newRole);

        assertThat(savedRole.getId()).isNotNull();
        assertThat(savedRole.getName()).isEqualTo(RoleEnum.CLIENT);
        assertThat(savedRole.getDescription()).isEqualTo("Role for clients");
    }

    @Test
    void testRoleNotFound() {
        Optional<Role> foundRole = roleRepository.findByName(RoleEnum.LOGISTICIEN);

        assertThat(foundRole).isNotPresent();
    }
}
