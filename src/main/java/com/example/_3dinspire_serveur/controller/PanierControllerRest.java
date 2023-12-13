package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Panier;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.model.service.UtilisateurService;
import com.example._3dinspire_serveur.repository.PanierRepository;
import com.example._3dinspire_serveur.repository.PublicationRepository;
import com.example._3dinspire_serveur.repository.UserRespository;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/panier")
public class PanierControllerRest {
    private UtilisateurService utilisateurRepository;
    private PanierRepository panierRepository;
    private PublicationRepository publicationRepository;

    public PanierControllerRest(UtilisateurService utilisateurRepository, PanierRepository panierRepository, PublicationRepository publicationRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.panierRepository = panierRepository;
        this.publicationRepository = publicationRepository;
    }

    @GetMapping("/getPublicationPanier")
    public ResponseEntity<Iterable<Publication>> getPanier(@RequestParam("email") String email) {
        Iterable<Publication> publications = panierRepository.getPanier(email);
        ResponseEntity<Iterable<Publication>> responseEntity = ResponseEntity.ok().body(publications);
        responseEntity.getHeaders().forEach((headerName, headerValues) ->
                System.out.println(headerName + ": " + headerValues));

        return responseEntity;
    }
    @PostMapping("/createPanier")
    public Panier getOrCreatePanierForUser(String email) {
        Utilisateur utilisateur = utilisateurRepository.findUserByEmail(email);

        if (utilisateur != null) {
            Panier panier = panierRepository.findPanierByUtilisateur(utilisateur);

            if (panier == null) {
                panier = new Panier();
                panier.setProprietaire(utilisateur);
                panier.setPrixTT(0);
                panier.setEtat(false);

                panier = panierRepository.save(panier);
            }

            return panier;
        } else {
            System.out.println("Utilisateur non trouvé pour l'email : " + email);
            return null;
        }
    }

    @GetMapping("/getPrix")
    public Float getPrixByPanier(@RequestParam("email") String email) {
        Utilisateur utilisateur = utilisateurRepository.findUserByEmail(email);
        if (utilisateur != null) {
            Panier panier = panierRepository.findPanierByUtilisateur(utilisateur);

            if (panier != null) {
                System.out.println(panier.getPrixTT());
                return panier.getPrixTT();
            }
            else{
                System.out.println("Panier non trouvé pour l'email : " + email);
                return null;
            }
        } else {
            System.out.println("Utilisateur non trouvé pour l'email : " + email);
            return null;
        }
    }
}

