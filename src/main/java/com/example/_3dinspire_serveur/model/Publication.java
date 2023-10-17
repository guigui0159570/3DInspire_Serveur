package com.example._3dinspire_serveur.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Publication {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String titre;

    @NotBlank
    private String description;

    @NotNull
    private boolean gratuit;
    @NotNull
    private boolean publique;

    private float prix;
    @NotBlank
    private String image;

    @NotNull
    private int nb_telechargement;

    @OneToMany(mappedBy = "publication")
    private List<Avis> books = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "publication_panier",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "panier_id")
    )
    private Set<Panier> paniers = new HashSet<>();

    @OneToOne(mappedBy = "publication_notif")
    private Notification notification;

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isGratuit() {
        return gratuit;
    }

    public void setGratuit(boolean gratuit) {
        this.gratuit = gratuit;
    }

    public boolean isPublique() {
        return publique;
    }

    public void setPublique(boolean publique) {
        this.publique = publique;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNb_telechargement() {
        return nb_telechargement;
    }

    public void setNb_telechargement(int nb_telechargement) {
        this.nb_telechargement = nb_telechargement;
    }

    public List<Avis> getBooks() {
        return books;
    }

    public void setBooks(List<Avis> books) {
        this.books = books;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
