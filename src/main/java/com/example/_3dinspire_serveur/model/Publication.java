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

    private LocalDateTime dateLocal;

}
