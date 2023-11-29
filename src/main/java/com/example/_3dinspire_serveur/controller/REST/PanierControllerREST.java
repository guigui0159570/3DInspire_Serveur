package com.example._3dinspire_serveur.controller.REST;

import com.example._3dinspire_serveur.model.Panier;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.PanierRepository;
import com.example._3dinspire_serveur.repository.PublicationRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/panier")
public class PanierControllerREST {
    private final PanierRepository panierRepository;

    public PanierControllerREST(PanierRepository panierRepository) {
        this.panierRepository = panierRepository;
    }

    @PostMapping("/creationPanier")
    public Panier creerPanier(@ModelAttribute@Valid Panier panier){
        return panierRepository.save(panier);
    }


    @PostMapping("/resultSearchPanier")
    public Panier searchPanier(@ModelAttribute@Valid Utilisateur user){
        return panierRepository.findPanierByUser(user.getEmail());
    }

}
