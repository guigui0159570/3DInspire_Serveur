package com.example._3dinspire_serveur.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Preferences {
    @Id
    private Long id;
    @OneToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
