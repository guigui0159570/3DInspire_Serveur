package com.example._3dinspire_serveur.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
@Entity
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String message;

    @NotNull
    private LocalDate date;

    @OneToOne
    @JoinColumn(name = "publication_id")
    private Publication publication_notif;



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

    public Publication getPublication() {
        return publication_notif;
    }

    public void setPublication(Publication publication) {
        this.publication_notif = publication;
    }
}
