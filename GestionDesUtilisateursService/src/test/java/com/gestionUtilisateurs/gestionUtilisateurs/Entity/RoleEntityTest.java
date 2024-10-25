package com.gestionUtilisateurs.gestionUtilisateurs.Entity;

import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleEntityTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testSaveRole() {
        Role role = new Role();
        role.setName(RoleEnum.CONSEILLER);
        role.setDescription("Conseiller role");

        Role savedRole = roleRepository.save(role);

        assertNotNull(savedRole);
        assertNotNull(savedRole.getId());
        assertEquals(RoleEnum.CONSEILLER, savedRole.getName());
        assertEquals("Conseiller role", savedRole.getDescription());
    }
}
