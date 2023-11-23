package com.example._3dinspire_serveur.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Tag {
    @Id
    @NotBlank
    private String nom;

    @ManyToOne
    @JoinColumn(name = "publication_id")
    private Publication publication;



    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}