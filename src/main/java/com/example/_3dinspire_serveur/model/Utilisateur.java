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
            name = "Abonnements",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "abonnement_id")
    )
    private Set<Utilisateur> Abonnements;

    @ManyToMany
    @JoinTable(
            name = "Abonnes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "abonne_id")
    )
    private Set<Utilisateur> Abonnes;

    @OneToOne
    @JoinColumn(name="profil_id")
    private Profil profil;

    @OneToMany(mappedBy = "proprietaire")
    @JsonIgnore
    private Set<Publication> publications;


    public void ajouterAbonnement(Utilisateur user){
        this.Abonnements.add(user);
    }

    public void ajouterAbonne(Utilisateur user){
        this.Abonnes.add(user);
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
        return Abonnements;
    }

    public Integer countAbonnement(){
        return getAbonnements().size();
    }
    public void setAbonnements(Set<Utilisateur> abonnements) {
        Abonnements = abonnements;
    }

    public Set<Utilisateur> getAbonnes() {
        return Abonnes;
    }

    public Integer countAbonne(){
        return getAbonnes().size();
    }

    public void setAbonnes(Set<Utilisateur> abonnes) {
        Abonnes = abonnes;
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
