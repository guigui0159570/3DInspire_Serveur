package com.example._3dinspire_serveur.model.DTO;

import com.example._3dinspire_serveur.model.Avis;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

public class UtilisateurDTO {
    private Long id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String pseudo;
    @NotBlank
    private String password;

    // No password field in DTO to enhance security

    private Set<Long> abonnementsIds;
    private Set<Long> abonnesIds;
    private Set<Long> publicationsIds;
    private Set<Long> avisIds;

    // Getters and setters


    public UtilisateurDTO(String email, String pseudo, String password) {
        this.email = email;
        this.pseudo = pseudo;
        this.password = password;
    }

    public UtilisateurDTO(Long id, String email, String pseudo, Set<Utilisateur> abonnementsIds, Set<Utilisateur> abonnesIds, Set<Publication> publicationsIds, Set<Avis> avisIds) {
        this.id = id;
        this.email = email;
        this.pseudo = pseudo;
        setAbonnementsIds(abonnementsIds);
        setAbonnesIds(abonnesIds);
        setAvisIds(avisIds);
        setPublicationsIds(publicationsIds);
    }
    public UtilisateurDTO(Utilisateur utilisateur) {
        this.id = utilisateur.getId();
        this.email = utilisateur.getEmail();
        this.pseudo = utilisateur.getPseudo();
        setAbonnementsIds(utilisateur.getAbonnements());
        setAbonnesIds(utilisateur.getAbonnes());
        setAvisIds(utilisateur.getAvis());
        setPublicationsIds(utilisateur.getPublications());
    }

    public UtilisateurDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Set<Long> getAbonnementsIds() {
        return abonnementsIds;
    }

    public void setAbonnementsIds(Set<Utilisateur> abonnementsIds) {
        for(Utilisateur u:abonnementsIds){
            this.abonnementsIds.add(u.getId());
        }
    }

    public Set<Long> getAbonnesIds() {
        return abonnesIds;
    }

    public void setAbonnesIds(Set<Utilisateur> abonnesIds) {
        for (Utilisateur u:abonnesIds){
            this.abonnesIds.add(u.getId());
        }
    }

    public Set<Long> getPublicationsIds() {
        return publicationsIds;
    }

    public void setPublicationsIds(Set<Publication> publications) {
        for (Publication p:publications) {
            this.publicationsIds.add(p.getId());
        }
    }

    public Set<Long> getAvisIds() {
        return avisIds;
    }

    public void setAvisIds(Set<Avis> avis) {
        for (Avis v:avis) {
            this.avisIds.add(v.getId());
        }
    }
}
