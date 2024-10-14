package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.LoginUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Client;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.model.UtilisateurRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImp implements AuthServiceItf {
    private final UtilisateurRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthServiceImp(
            UtilisateurRepository userRepository, RoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Utilisateur signup(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.CLIENT);


        if (optionalRole.isEmpty()) {
            return null;
        }

        Client user = new Client();
        user.setNom(input.getNom());
        user.setPrenom(input.getPrenom());
        user.setEmail(input.getEmail());
        user.setAdresse(input.getAdresse());
        user.setNumTel(input.getNumTel());
        user.setDateNaissance(input.getDateNaissance());
        user.setMotDePasse(passwordEncoder.encode(input.getPassword()));
        user.setRole(optionalRole.get());
            System.out.println(user);
        return userRepository.save(user);
    }

    @Override
    public Utilisateur authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}

