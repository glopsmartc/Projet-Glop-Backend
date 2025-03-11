package com.gestionUtilisateurs.gestionUtilisateurs.bootstrap;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Partenaire;
import com.gestionUtilisateurs.gestionUtilisateurs.model.PartenaireRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PartenaireSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final PartenaireRepository partenaireRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${logisticien.password}")
    private String motDePasse;

    public PartenaireSeeder(
            RoleRepository roleRepository, PartenaireRepository partenaireRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.partenaireRepository = partenaireRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createPartenaires();
    }

    public void createPartenaires() {
        List<RegisterUserDto> users = List.of(
                new RegisterUserDto("suzanne@email.com", motDePasse, "dupont", "Suzanne", "+628123456789", "Jakarta, Indonisie", "Feminin"),
                new RegisterUserDto("marc@email.com", motDePasse, "legrand", "Marc", "+17987654345", "Toronto, Canada", "Masculin"),
                new RegisterUserDto("fatima@email.com", motDePasse, "bennani", "Fatima", "+2126654321", "Casablanca, Maroc", "Feminin"),
                new RegisterUserDto("lucas@email.com", motDePasse, "fernandez", "Lucas", "+551199887766", "São Paulo, Brésil", "Masculin")
        );

        List<String> entreprises = List.of(
                "Logistique Indonisie",
                "Tech Solutions Canada",
                "Bennani Commerce",
                "Fernandez Industries"
        );

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.PARTENAIRE);
        if (optionalRole.isEmpty()) {
            return;
        }

        for (int i = 0; i < users.size(); i++) {
            RegisterUserDto userDto = users.get(i);
            Optional<Partenaire> existingPartenaire = Optional.ofNullable(partenaireRepository.findByEmail(userDto.getEmail()));
            if (existingPartenaire.isPresent()) {
                continue;
            }

            Partenaire partenaire = new Partenaire();
            partenaire.setNom(userDto.getNom());
            partenaire.setPrenom(userDto.getPrenom());
            partenaire.setEmail(userDto.getEmail());
            partenaire.setAdresse(userDto.getAdresse());
            partenaire.setNumTel(userDto.getNumTel());
            partenaire.setSexe(userDto.getSexe());
            partenaire.setMotDePasse(passwordEncoder.encode(userDto.getPassword()));
            partenaire.setRole(optionalRole.get());

            partenaire.setZoneGeographique(userDto.getAdresse().split(",")[1].trim());
            partenaire.setNomEntreprise(entreprises.get(i));

            partenaireRepository.save(partenaire);
        }
    }
}
