package com.example._3dinspire_serveur.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    private String message;
    private LocalDate date;

    @ManyToOne
    @JoinColumn
    private Utilisateur utilisateur;

//    @OneToOne
//    @JoinColumn(name = "publication_id")
//    private Publication publication_notif;


    public Notification() {
    }

    public Notification(String message, LocalDate date, Utilisateur utilisateur) {
        this.message = message;
        this.date = date;
        this.utilisateur = utilisateur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    //    public Publication getPublication() {
//        return publication_notif;
//    }
//
//    public void setPublication(Publication publication) {
//        this.publication_notif = publication;
//    }
}
