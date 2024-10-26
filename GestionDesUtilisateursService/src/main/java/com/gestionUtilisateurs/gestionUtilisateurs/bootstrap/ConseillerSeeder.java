package com.gestionUtilisateurs.gestionUtilisateurs.bootstrap;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.model.UtilisateurRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import jdk.jshell.execution.Util;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Component
public class ConseillerSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UtilisateurRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public ConseillerSeeder(
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
        this.createConseiller();
    }

    private void createConseiller() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setNom("Super conseiller");
        userDto.setEmail("super.conseiller@email.com");
        userDto.setPassword("123456");
        userDto.setPrenom("rawan");
        userDto.setAdresse("LILLE");
        userDto.setDateNaissance(LocalDate.parse("12-10-2020", formatter));
        userDto.setNumTel("+3345455667");


        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.CONSEILLER);
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
        user.setDateNaissance(userDto.getDateNaissance());
        user.setMotDePasse(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(optionalRole.get());
        System.out.println(user);

        userRepository.save(user);
    }
}
