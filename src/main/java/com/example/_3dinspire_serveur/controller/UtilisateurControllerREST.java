package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Panier;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.PanierRepository;
import com.example._3dinspire_serveur.repository.RoleRepository;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Iterator;
import java.util.Optional;

@RestController
public class UtilisateurControllerREST {
    private final UtilisateurRepository utilisateurRepository;

    private final PanierRepository panierRepository;
    @PersistenceContext
    private EntityManager entityManager;

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

    @GetMapping("/getUtilisateurIdByEmail")
    public Long getUtilisateurIdByEmail(@RequestParam String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);
        return utilisateur.getId();
    }

    @Transactional
    @DeleteMapping("/utilisateur/delete/{id}")
    public void deleteUtilisateur( @PathVariable("id") Long id) {
        try {
            utilisateurRepository.deleteById(id);
            System.out.println("Utilisateur supprimé avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur.");
        }
    }


}
