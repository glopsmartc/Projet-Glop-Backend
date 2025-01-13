package com.gestioncontrats.gestioncontrats.repository;

import com.gestioncontrats.gestioncontrats.model.Contrat;
import com.gestioncontrats.gestioncontrats.model.ContratRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class ContratRepositoryTest {

    @Autowired
    private ContratRepository contratRepository;

    private Contrat contrat;

    @BeforeEach
    void setUp() {
        contrat = new Contrat();
        contrat.setClient("Client1");
        contrat.setDureeContrat("12 mois");
        contratRepository.save(contrat);
    }

    @Test
    void testFindByClient() {
        List<Contrat> contrats = contratRepository.findByClient("Client1");
        assertThat(contrats).isNotEmpty();
        assertThat(contrats.get(0).getClient()).isEqualTo("Client1");
    }

    @Test
    void testFindByNonExistingClient() {
        List<Contrat> contrats = contratRepository.findByClient("NonExistingClient");
        assertThat(contrats).isEmpty();
    }
}
