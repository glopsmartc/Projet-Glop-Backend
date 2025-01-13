package com.gestioncontrats.gestioncontrats.repository;

import com.gestioncontrats.gestioncontrats.model.Offre;
import com.gestioncontrats.gestioncontrats.model.OffreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OffreRepositoryTest {

    @Autowired
    private OffreRepository offreRepository;

    private Offre offre;

    @BeforeEach
    void setUp() {
        offre = new Offre();
        offre.setNomOffre("Offre Test");
        offreRepository.save(offre);
    }

    @Test
    void testExistsByNomOffre() {
        boolean exists = offreRepository.existsByNomOffre("Offre Test");
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByNonExistingNomOffre() {
        boolean exists = offreRepository.existsByNomOffre("NonExistingOffer");
        assertThat(exists).isFalse();
    }

    @Test
    void testFindByNonExistingNomOffre() {
        Offre foundOffre = offreRepository.findByNomOffre("NonExistingOffer");
        assertThat(foundOffre).isNull();
    }
}
