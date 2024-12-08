package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.LoginUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Client;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;
import com.gestionUtilisateurs.gestionUtilisateurs.model.UtilisateurRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleEnum;
import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.RoleRepository;
import com.gestionUtilisateurs.gestionUtilisateurs.email.EmailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImp implements AuthServiceItf {
    private final UtilisateurRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private EmailService emailService;

    public AuthServiceImp(
            UtilisateurRepository userRepository, RoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, EmailService emailService
    ) {
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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
        //user.setDateNaissance(input.getDateNaissance());
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

    @Override
    public void initiatePasswordReset(String email) {
        Optional<Utilisateur> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();

            // Créer un token de réinitialisation
            String resetToken = UUID.randomUUID().toString();

            // Définir une date d'expiration du token
            LocalDateTime expirationTime = LocalDateTime.now().plusHours(24);

            // Sauvegarder le token et la date d'expiration dans l'utilisateur
            user.setResetToken(resetToken);
            user.setResetTokenExpiration(expirationTime);

            // Sauvegarder l'utilisateur mis à jour
            userRepository.save(user);

            // Envoyer l'email avec le token
            emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
        } else {
            throw new RuntimeException("Utilisateur non trouvé");
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        Optional<Utilisateur> userOpt = userRepository.findByResetToken(token);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();

            // Vérifier si le token est encore valide
            if (user.getResetTokenExpiration().isAfter(LocalDateTime.now())) {
                // Réinitialiser le mot de passe
                user.setMotDePasse(passwordEncoder.encode(newPassword));

                // Effacer le token et la date d'expiration
                user.setResetToken(null);
                user.setResetTokenExpiration(null);

                // Sauvegarder les modifications
                userRepository.save(user);
            } else {
                throw new RuntimeException("Le token a expiré.");
            }
        } else {
            throw new RuntimeException("Token invalide.");
        }
    }

}

