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
    private Set<Utilisateur> abonnements = new HashSet<>();
    @ManyToMany(mappedBy = "abonnements")
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
    private List<Role> roles = new ArrayList<>();

    public Utilisateur(String email, String pseudo, String password) {
        this.email = email;
        this.pseudo = pseudo;
        this.password = password;
    }
}
