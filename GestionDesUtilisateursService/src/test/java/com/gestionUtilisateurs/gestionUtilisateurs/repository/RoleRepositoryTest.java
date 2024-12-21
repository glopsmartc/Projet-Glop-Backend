package com.gestionUtilisateurs.gestionUtilisateurs.repository;

import com.gestionUtilisateurs.gestionUtilisateurs.GestionDesUtilisateursServiceApplication;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = GestionDesUtilisateursServiceApplication.class)
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        if (!roleRepository.findByName(RoleEnum.USER).isPresent()) {
            Role userRole = new Role();
            userRole.setName(RoleEnum.USER);
            userRole.setDescription("Role for regular users");
            roleRepository.save(userRole);
        }

        if (!roleRepository.findByName(RoleEnum.MEDECIN).isPresent()) {
            Role adminRole = new Role();
            adminRole.setName(RoleEnum.MEDECIN);
            adminRole.setDescription("Role for medical staff");
            roleRepository.save(adminRole);
        }
    }


    @Test
    void testFindByName() {
        Optional<Role> foundRole = roleRepository.findByName(RoleEnum.USER);

        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo(RoleEnum.USER);
        assertThat(foundRole.get().getDescription()).isEqualTo("Role for regular users");
    }

    @Test
    @Sql(scripts = "/reset_roles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
    @Sql(scripts = "/reset_roles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testRoleNotFound() {
        Optional<Role> foundRole = roleRepository.findByName(RoleEnum.LOGISTICIEN);

        assertThat(foundRole).isNotPresent();
    }
}
