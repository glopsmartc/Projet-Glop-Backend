package com.gestioncontrats.gestioncontrats.model;

import ch.qos.logback.core.net.server.Client;
import com.gestioncontrats.gestioncontrats.dto.UtilisateurDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "contrat", schema = "gestion_contrats")
@NoArgsConstructor
@Getter
@Setter
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dureeContrat;
    private String statut;
    private boolean assurerTransport;
    private boolean assurerPersonnes;
    private boolean voiture;
    private boolean trotinette;
    private boolean bicyclette;
    private Integer nombrePersonnes;
    private LocalDate debutContrat;
    private String destination;
    private LocalDate dateAller;
    private LocalDate dateRetour;
    private String numeroTelephone;
    private LocalDate dateNaissanceSouscripteur;

    private String pdfPath;

    private String price;

    private String client; //email

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contrat_id")
    private List<Accompagnant> accompagnants;

    @ManyToOne
    @JoinColumn(name = "offre_id")
    private Offre offre;
}
