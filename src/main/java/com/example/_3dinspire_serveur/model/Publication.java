package com.example._3dinspire_serveur.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
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
    @NotBlank
    private String fichier;

    @NotNull
    private int nb_telechargement;

    @OneToMany(mappedBy = "publication")
    @JsonIgnore
    private List<Avis> avis = new ArrayList<>();

    //@JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "publication_panier",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "panier_id")
    )
    private Set<Panier> paniers = new HashSet<>();

    @OneToOne(mappedBy = "publication_notif")
    private Notification notification;

    @ManyToOne
    @JoinColumn
    private Utilisateur proprietaire;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "publication_tag",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public Publication() {
    }

    public Publication(Long id, String titre, String description, boolean gratuit, boolean publique, float prix, String image, String fichier, int nb_telechargement, List<Avis> avis, Set<Panier> paniers, Notification notification, Utilisateur proprietaire) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.gratuit = gratuit;
        this.publique = publique;
        this.prix = prix;
        this.image = image;
        this.fichier = fichier;
        this.nb_telechargement = nb_telechargement;
        this.avis = avis;
        this.paniers = paniers;
        this.notification = notification;
        this.proprietaire = proprietaire;
    }

    public Set<Panier> getPaniers() {
        return paniers;
    }


    private LocalDateTime dateLocal;

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Utilisateur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Utilisateur proprietaire) {
        this.proprietaire = proprietaire;
    }

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

    public List<Avis> getAvis() {
        return avis;
    }

    public void setAvis(List<Avis> books) {
        this.avis = books;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public Integer notation_publication(){
        int sum = 0;
        for(Avis i : avis){
            sum += i.getEtoile();
        }
        return Math.round((float) sum /avis.size());
    }
}
