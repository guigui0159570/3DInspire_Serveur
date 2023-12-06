package com.example._3dinspire_serveur.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "utilisateur")
public class Utilisateur {
    @Id
    @GeneratedValue
    private Long id;
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotBlank
    private String pseudo;
    @NotBlank
    private String password;
    private String resetToken;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "Abonnements",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "abonnement_id")
    )
    @JsonIgnore
    private Set<Utilisateur> Abonnements;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "Abonnes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "abonne_id")
    )
    @JsonIgnore
    private Set<Utilisateur> Abonnes;

    @OneToOne
    @JoinColumn(name="profil_id")
    @JsonIgnore
    private Profil profil;

    @OneToMany(mappedBy = "proprietaire")
    @JsonIgnore
    private Set<Publication> publications;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name="users_roles",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")})
    @JsonIgnore
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Avis> avis;

    public boolean verifAbonnement(Utilisateur user){
        return this.getAbonnements().contains(user);
    }
    public void deleteAbonnement(Utilisateur user){
        getAbonnements().remove(user);
    }

    @OneToOne @JoinColumn(name = "panier_user")
    @JsonIgnore
    private Panier panier;

    public void deleteAbonne(Utilisateur user){
        getAbonnes().remove(user);
    }

    public Set<Avis> getAvis() {
        return avis;
    }

    public void setAvis(Set<Avis> avis) {
        this.avis = avis;
    }

    public void ajouterAbonnement(Utilisateur user){
        this.Abonnements.add(user);
    }

    public void ajouterAbonne(Utilisateur user){
        this.Abonnes.add(user);
    }

    public Set<Publication> getPublications() {
        return publications;
    }

    public Utilisateur(String email, String pseudo, String password) {
        this.email = email;
        this.pseudo = pseudo;
        this.password = password;
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

    public Integer  countAbonnement(){
        return getAbonnements().size();
    }

    public Integer countAbonne(){
        return getAbonnes().size();
    }

}
