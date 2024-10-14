package com.gestionUtilisateurs.gestionUtilisateurs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginResponse {
    private String token;

    private long expiresIn;

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setToken(String token) {
        this.token = token;
    }
}