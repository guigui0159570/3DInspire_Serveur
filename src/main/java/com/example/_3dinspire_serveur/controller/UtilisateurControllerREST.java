package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Profil;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.ProfilRepository;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UtilisateurControllerREST {
    private final UtilisateurRepository utilisateurRepository;
    private final ProfilRepository profilRepository;

    public UtilisateurControllerREST(UtilisateurRepository utilisateurRepository, ProfilRepository profilRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.profilRepository = profilRepository;
    }


    @PostMapping("/saveUtilisateur")
    public Utilisateur ajoutUtilisateur(@ModelAttribute@Valid Utilisateur utilisateur, @ModelAttribute@Valid Profil profil){
        profilRepository.save(profil);
        utilisateur.setProfil(profil);
        return utilisateurRepository.save(utilisateur);
    }

    @GetMapping("/abonnenement/{user}/{id}")
    public void abonnenement(@PathVariable Long id, @PathVariable Long user){
        Optional<Utilisateur> utilisateur_abonne = utilisateurRepository.findById(id);
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur_abonne.isPresent() && utilisateur.isPresent()){
            utilisateur.get().ajouterAbonnement(utilisateur_abonne.get());
            utilisateur_abonne.get().ajouterAbonne(utilisateur.get());
            utilisateurRepository.save(utilisateur.get());
            utilisateurRepository.save(utilisateur_abonne.get());
        }
    }

    @GetMapping("/countAbonnement/{user}")
    public Integer countAbonnement(@PathVariable Long user){
        Integer count = 0;
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur.isPresent()){
            count = utilisateur.get().countAbonnement();
        }
        return count;
    }
}

