package com.example._3dinspire_serveur.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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


    @ManyToMany(mappedBy = "paniers")
    private Set<Publication> publications = new HashSet<>();


    public Panier(float prixTT, boolean etat, Set<Publication> publications) {
        this.prixTT = prixTT;
        this.etat = etat;
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
}
