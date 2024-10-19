package com.gestionUtilisateurs.gestionUtilisateurs.service;

import com.gestionUtilisateurs.gestionUtilisateurs.dto.LoginUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.dto.RegisterUserDto;
import com.gestionUtilisateurs.gestionUtilisateurs.model.Utilisateur;


public interface AuthServiceItf {

    Utilisateur signup(RegisterUserDto input);

    Utilisateur authenticate(LoginUserDto input);

    // Crée un token temporaire et envoie l'email de réinitialisation
    void initiatePasswordReset(String email);

    void resetPassword(String token, String newPassword);
}
