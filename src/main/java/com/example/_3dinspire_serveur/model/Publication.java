package com.example._3dinspire_serveur.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor
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

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinTable(
            name = "publication_panier",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "panier_id")
    )
    @JsonIgnore
    private Set<Panier> paniers = new HashSet<>();

//    @OneToOne(mappedBy = "publication_notif")
//    private Notification notification;

    @ManyToOne
    @JoinColumn
    private Utilisateur proprietaire;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "publication_tag",
            joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    private LocalDateTime dateLocal;

    public Publication(String titre, String description, boolean gratuit, boolean publique, float prix, String image, String fichier, int nb_telechargement, Utilisateur proprietaire, LocalDateTime dateLocal) {
        this.titre = titre;
        this.description = description;
        this.gratuit = gratuit;
        this.publique = publique;
        this.prix = prix;
        this.image = image;
        this.fichier = fichier;
        this.nb_telechargement = nb_telechargement;
        this.notification = notification;
        this.proprietaire = proprietaire;
        this.dateLocal = dateLocal;
    }
}
