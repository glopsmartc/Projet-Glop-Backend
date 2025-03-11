package com.gestionUtilisateurs.gestionUtilisateurs.bootstrap;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Medecin;
import com.gestionUtilisateurs.gestionUtilisateurs.model.MedecinRepository;
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
public class MedecinSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final MedecinRepository medecinRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${medecin.password}")
    private String motDePasse;

    public MedecinSeeder(
            RoleRepository roleRepository,
            MedecinRepository medecinRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.medecinRepository = medecinRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createMedecin();
    }

    public void createMedecin() {
        // Initialisation des informations du médecin
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setNom("medecin_1");
        userDto.setEmail("medecin1@email.com");
        userDto.setPassword(motDePasse);
        userDto.setPrenom("Jean");
        userDto.setAdresse("Lyon");
        userDto.setSexe("Masculin");
        userDto.setNumTel("+33612345678");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.MEDECIN);
        Optional<Medecin> existingMedecin = Optional.ofNullable(medecinRepository.findByEmail(userDto.getEmail()));

        if (optionalRole.isEmpty() || existingMedecin.isPresent()) {
            return;
        }

        // Création du médecin
        Medecin medecin = new Medecin();
        medecin.setNom(userDto.getNom());
        medecin.setPrenom(userDto.getPrenom());
        medecin.setEmail(userDto.getEmail());
        medecin.setAdresse(userDto.getAdresse());
        medecin.setNumTel(userDto.getNumTel());
        medecin.setSexe(userDto.getSexe());
        medecin.setMotDePasse(passwordEncoder.encode(userDto.getPassword()));  // Mot de passe encodé
        medecin.setRole(optionalRole.get());  // Assigner le rôle

        medecin.setSpecialite("Cardiologie");
        medecin.setDisponibilites(List.of("Lundi 9h-12h", "Mercredi 14h-17h"));
        medecin.setDeplacement(true);

        System.out.println(medecin);

        // Sauvegarder le médecin dans la base de données
        medecinRepository.save(medecin);
    }
}
