package com.example._3dinspire_serveur.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Model3D {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String chemin;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
