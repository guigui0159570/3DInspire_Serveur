package com.example._3dinspire_serveur.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToMany
    @JoinTable(
            name = "abonnements",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "abonne_id")
    )
    @JsonIgnore
    private Set<Utilisateur> abonnements = new HashSet<>();
    @ManyToMany(mappedBy = "abonnements")
    @JsonIgnore
    private Set<Utilisateur> abonnes = new HashSet<>();

    @OneToOne
    @JoinColumn(name="profil_id")
    private Profil profil;

    @OneToMany(mappedBy = "proprietaire")
    @JsonIgnore
    private Set<Publication> publications;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="users_roles",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")})
    @JsonIgnore
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)    @JsonIgnore
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

    public Utilisateur(String email, String pseudo, String password) {
        this.email = email;
        this.pseudo = pseudo;
        this.password = password;
    }

    public Long getId() {
        return id;
    }
<<<<<<< HEAD
=======

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
>>>>>>> panier
}
