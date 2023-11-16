package com.example._3dinspire_serveur.model.DTO;

public class AvisDTO {
    private long id;
    private String commentaire;
    private int etoile;
    private long publication_id;

    private long utilisateur_id;

    public long getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(long utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    public AvisDTO(long id, String commentaire, int etoile, long publication_id, long utilisateur_id) {
        this.id = id;
        this.commentaire = commentaire;
        this.etoile = etoile;
        this.publication_id = publication_id;
        this.utilisateur_id = utilisateur_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getEtoile() {
        return etoile;
    }

    public void setEtoile(int etoile) {
        this.etoile = etoile;
    }

    public long getPublication_id() {
        return publication_id;
    }

    public void setPublication_id(long publication_id) {
        this.publication_id = publication_id;
    }
}
