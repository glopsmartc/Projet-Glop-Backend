package com.gestionUtilisateurs.gestionUtilisateurs.bootstrap;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.model.UtilisateurRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LogisticienSeeder  implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${logisticien.password}")
    private String motDePasse;

    private final RoleRepository roleRepository;
    private final UtilisateurRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public LogisticienSeeder(
            RoleRepository roleRepository,
            UtilisateurRepository  userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createLogisticien();
    }

    public void createLogisticien() {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setNom("logisticien_1");
        userDto.setEmail("logisticien1@email.com");
        userDto.setPassword(motDePasse);
        userDto.setPrenom("salma");
        userDto.setAdresse("PARIS");
        userDto.setSexe("Feminin");
        userDto.setNumTel("+3354545445");


        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.LOGISTICIEN);
        Optional<Utilisateur> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        Utilisateur user = new Utilisateur();
        user.setNom(userDto.getNom());
        user.setPrenom(userDto.getPrenom());
        user.setEmail(userDto.getEmail());
        user.setAdresse(userDto.getAdresse());
        user.setNumTel(userDto.getNumTel());
        user.setSexe(userDto.getSexe());
        user.setMotDePasse(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(optionalRole.get());
        System.out.println(user);

        userRepository.save(user);
    }
}
