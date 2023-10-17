package com.example._3dinspire_serveur.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "avis")
public class Avis {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String commentaire;

    @NotNull
    private int etoile;

    @ManyToOne
    @JoinColumn(name = "publication_id")
    private Publication publication;
}
