package com.example._3dinspire_serveur.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "profil")
public class Profil {
    @Id
    @GeneratedValue
    private Long id;

    private String description;

    private String photo;

    @OneToOne(mappedBy = "profil")
    private Utilisateur utilisateur;

    public Profil(String description, byte[] photo, Utilisateur utilisateur) {
        this.description = description;
        this.photo = photo;
        this.utilisateur = utilisateur;
    }

    public Profil() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
