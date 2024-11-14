package com.gestionUtilisateurs.gestionUtilisateurs.dto;

import com.gestionUtilisateurs.gestionUtilisateurs.model.roles.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UtilisateurDTO {
    private String username;
    private String email;
    private String nom;
    private String prenom;
    private String role;

}

