package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.dto.MaladieChroniqueDTO;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Client;
import com.gestionUtilisateurs.gestionUtilisateurs.model.ClientRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.model.UtilisateurRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserServiceItf {
    private final UtilisateurRepository userRepository;

    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);


    public UserServiceImp(UtilisateurRepository userRepository, ClientRepository clientRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Utilisateur> allUsers() {
        List<Utilisateur> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    @Override
    public Utilisateur createConseiller(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.CONSEILLER);

        if (optionalRole.isEmpty()) {
            return null;
        }

        Utilisateur user = new Utilisateur();
        user.setNom(input.getNom());
        user.setPrenom(input.getPrenom());
        user.setEmail(input.getEmail());
        user.setAdresse(input.getAdresse());
        user.setNumTel(input.getNumTel());
        user.setSexe(input.getSexe());
        //user.setDateNaissance(input.getDateNaissance());
        user.setMotDePasse(passwordEncoder.encode(input.getPassword()));
        user.setRole(optionalRole.get());
        System.out.println(user);

        return userRepository.save(user);
    }

    @Override
    public void updateDateNaissance(String email, LocalDate dateNaissance) {
        Utilisateur utilisateur = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé pour l'email : " + email));

        utilisateur.setDateNaissance(dateNaissance);

        userRepository.save(utilisateur);
    }

    @Override
    public boolean updateMaladieChronique(MaladieChroniqueDTO maladieChroniqueDTO) {
        Optional<Client> utilisateurOpt = Optional.ofNullable(clientRepository.findByEmail(maladieChroniqueDTO.getEmail()));

        if (utilisateurOpt.isPresent()) {
            Client client = utilisateurOpt.get();
            client.setMaladieChronique(maladieChroniqueDTO.getMaladieChronique());
            client.setDescriptionMaladie(maladieChroniqueDTO.getDescriptionMaladie());

            clientRepository.save(client);
            log.info("Maladie chronique mise à jour pour l'utilisateur EMAIL {}", utilisateurOpt.get().getEmail());
            return true;
        } else {
            log.warn("Utilisateur non trouvé avec l'email {}", utilisateurOpt.get().getEmail());
            return false;
        }
    }

}
