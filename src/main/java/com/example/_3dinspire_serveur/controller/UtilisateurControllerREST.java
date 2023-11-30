package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Panier;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.PanierRepository;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;

@RestController
public class UtilisateurControllerREST {
    private final UtilisateurRepository utilisateurRepository;

    private final PanierRepository panierRepository;

    public UtilisateurControllerREST(UtilisateurRepository utilisateurRepository, PanierRepository panierRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.panierRepository = panierRepository;
    }

    @PostMapping("/saveUtilisateur")
    public Utilisateur saveUtilisateur(@ModelAttribute@Valid Utilisateur utilisateur){
        // Récupère les paniers qui ne sont pas assignés
        Iterable<Panier> paniers = panierRepository.findPanierByEtat(false);
        Iterator<Panier> ite = paniers.iterator();
        // Regarde s'il y a au moins un panier de libre
        if(ite.hasNext()){
            for (Panier panier : paniers) {
                utilisateur.setPanier(panier);
                panier.setEtat(true);
                break;
            }
        }// Faire un redirect vers la page de création de panier
        return utilisateurRepository.save(utilisateur);
    }

    @GetMapping("/getAllUtilisateurs")
    public Iterable<Utilisateur> getAllUtilisateur(){
        return utilisateurRepository.findAll();
    }
}
