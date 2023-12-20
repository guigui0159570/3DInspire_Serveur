package com.example._3dinspire_serveur.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Panier {
    @Id
    @GeneratedValue
    private Long idPanier;
    @NotNull
    private float prixTT;
    @NotNull
    private boolean etat;

    @NotNull
    @ManyToOne
    @JoinColumn
    private Utilisateur utilisateur;


    @ManyToMany(mappedBy = "paniers",cascade = {CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH})
    private Set<Publication> publications = new HashSet<>();


    public Panier(float prixTT, boolean etat, Utilisateur proprietaire, Set<Publication> publications) {
        this.prixTT = prixTT;
        this.etat = etat;
        this.utilisateur = proprietaire;
        this.publications = publications;
    }

    public Panier() {
    }

    public Long getIdPanier() {
        return idPanier;
    }

    public void setIdPanier(Long idPanier) {
        this.idPanier = idPanier;
    }

    public float getPrixTT() {
        return prixTT;
    }

    public void setPrixTT(float prixTT) {
        this.prixTT = prixTT;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public Set<Publication> getPublications() {
        return publications;
    }

    public void setPublications(Set<Publication> publications) {
        this.publications = publications;
    }

    public Utilisateur getProprietaire() {
        return utilisateur;
    }

    public void setProprietaire(Utilisateur proprietaire) {
        this.utilisateur = proprietaire;
    }
}
