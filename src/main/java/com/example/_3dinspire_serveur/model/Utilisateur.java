package com.example._3dinspire_serveur.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {
    @Id
    @GeneratedValue
    private Long id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String pseudo;
    @NotBlank
    private String password;

    @ManyToMany
    @JoinTable(
            name = "abonnements",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "abonne_id")
    )
    private Set<Utilisateur> abonnements = new HashSet<>();
    @ManyToMany(mappedBy = "abonnements")
    private Set<Utilisateur> abonnes = new HashSet<>();

    @OneToOne
    @JoinColumn(name="profil_id")
    private Profil profil;

    @OneToMany(mappedBy = "proprietaire")
    @JsonIgnore
    private Set<Publication> publications;

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private Set<Avis> avis;

    @OneToOne @JoinColumn(name = "panier_user")
    private Panier panier;

    public Set<Avis> getAvis() {
        return avis;
    }

    public void setAvis(Set<Avis> avis) {
        this.avis = avis;
    }

    public Set<Publication> getPublications() {
        return publications;
    }

    public void setPublications(Set<Publication> publications) {
        this.publications = publications;
    }

    public Profil getProfil() {
        return profil;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Set<Utilisateur> getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(Set<Utilisateur> abonnements) {
        this.abonnements = abonnements;
    }

    public Set<Utilisateur> getAbonnes() {
        return abonnes;
    }

    public void setAbonnes(Set<Utilisateur> abonnes) {
        this.abonnes = abonnes;
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

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }
}
