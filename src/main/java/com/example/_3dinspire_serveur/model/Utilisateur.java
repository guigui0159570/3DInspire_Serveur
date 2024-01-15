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
    @JsonIgnore
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "UtilisateurNotifie",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "utilisateur_notifie_id")
    )
    @JsonIgnore
    private Set<Utilisateur> utilisateursNotifies;

    @OneToOne
    @JoinColumn(name="profil_id")
    @JsonIgnore
    private Profil profil;

    @OneToMany(mappedBy = "proprietaire")
    @JsonIgnore
    private Set<Publication> publications;

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private Set<Notification> notifications;

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

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private Set<Panier> paniers;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "utilisateur_achat",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "publication_id")
    )
    @JsonIgnore
    private Set<Publication> publicationsAchats;
    public Set<Utilisateur> getUtilisateursNotifies() {
        return utilisateursNotifies;
    }

    public void setUtilisateursNotifies(Set<Utilisateur> utilisateursNotifies) {
        this.utilisateursNotifies = utilisateursNotifies;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public void ajouterNotification(Notification notif){
        this.notifications.add(notif);
    }

    public void deleteAbonne(Utilisateur user){
        getAbonnes().remove(user);
    }

    public void deleteUserNotifie(Utilisateur user){
        this.getUtilisateursNotifies().remove(user);
    }
    public boolean verifAbonnement(Utilisateur user){
        return this.getAbonnements().contains(user);
    }

    public boolean verifUserNotifies(Utilisateur user){
        return this.getUtilisateursNotifies().contains(user);
    }
    public void deleteAbonnement(Utilisateur user){
        getAbonnements().remove(user);
    }

    public Set<Avis> getAvis() {
        return avis;
    }

    public void setAvis(Set<Avis> avis) {
        this.avis = avis;
    }

    public void ajouterUserNotifie(Utilisateur user){
        this.utilisateursNotifies.add(user);
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

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", password='" + password + '\'' +
                ", resetToken='" + resetToken + '\'' +
                ", Abonnements=" + Abonnements +
                ", Abonnes=" + Abonnes +
                ", utilisateursNotifies=" + utilisateursNotifies +
                ", profil=" + profil +
                ", publications=" + publications +
                ", notifications=" + notifications +
                ", roles=" + roles +
                ", avis=" + avis +
                ", paniers=" + paniers +
                '}';
    }
}
