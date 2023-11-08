package com.example._3dinspire_serveur.model.DTO;

import com.example._3dinspire_serveur.model.Profil;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Role;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UtilisateurDTO {
    private Long id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String pseudo;
    @NotBlank
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
