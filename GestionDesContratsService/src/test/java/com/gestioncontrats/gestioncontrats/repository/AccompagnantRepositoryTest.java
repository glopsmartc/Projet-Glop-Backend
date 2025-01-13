package com.gestioncontrats.gestioncontrats.repository;

import com.gestioncontrats.gestioncontrats.model.Accompagnant;
import com.gestioncontrats.gestioncontrats.GestioncontratsApplication;
import com.gestioncontrats.gestioncontrats.model.AccompagnantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = GestioncontratsApplication.class)
@ActiveProfiles("test")
class AccompagnantRepositoryTest {

    @Autowired
    private AccompagnantRepository accompagnantRepository;

    @Test
    public void testSaveAndFindById() {
        Accompagnant accompagnant = new Accompagnant();
        accompagnant.setNom("Dupont");
        accompagnant.setPrenom("Jean");

        Accompagnant savedAccompagnant = accompagnantRepository.save(accompagnant);

        assertThat(savedAccompagnant).isNotNull();
        assertThat(savedAccompagnant.getId()).isNotNull();

        Accompagnant foundAccompagnant = accompagnantRepository.findById(savedAccompagnant.getId()).orElse(null);

        assertThat(foundAccompagnant).isNotNull();
        assertThat(foundAccompagnant.getNom()).isEqualTo("Dupont");
        assertThat(foundAccompagnant.getPrenom()).isEqualTo("Jean");
    }
}
