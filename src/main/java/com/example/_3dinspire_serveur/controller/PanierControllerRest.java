package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Panier;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.model.service.UtilisateurService;
import com.example._3dinspire_serveur.repository.*;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/panier")
public class PanierControllerRest {
    private UtilisateurService utilisateurRepository;
    private PanierRepository panierRepository;
    private AvisRepository avisRepository;
    private UserRespository userRespository;
    private PublicationRepository publicationRepository;

    public PanierControllerRest(UtilisateurService utilisateurRepository, PanierRepository panierRepository, PublicationRepository publicationRepository,AvisRepository avisRepository,UserRespository userRespository) {
        this.utilisateurRepository = utilisateurRepository;
        this.panierRepository = panierRepository;
        this.publicationRepository = publicationRepository;
        this.avisRepository =avisRepository;
        this.userRespository = userRespository;
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
    public Panier createPanierForUser(String email) {
        Utilisateur utilisateur = utilisateurRepository.findUserByEmail(email);

        if (utilisateur != null) {
            Panier panier = panierRepository.findPanierByUtilisateurAndEtatIsFalse(utilisateur);

            if (panier == null || panier.isEtat()==true) {
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
            Panier panier = panierRepository.findPanierByUtilisateurAndEtatIsFalse(utilisateur);

            if (panier != null) {
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

    @PostMapping("/ajoutPublication")
    public ResponseEntity<Void> ajoutPublicationPanier(@RequestParam("email") String email, @RequestParam("idPub") Long idPub) {
        Utilisateur utilisateur = utilisateurRepository.findUserByEmail(email);

        if (utilisateur == null) {
            System.out.println("Utilisateur non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Panier panier = panierRepository.findPanierByUtilisateurAndEtatIsFalse(utilisateur);
        Float prixAvantAjout = getPrixByPanier(email);

        // Créer un panier s'il n'existe pas déjà
        if (panier == null || panier.isEtat()) {
            createPanierForUser(email);
            panier = panierRepository.findPanierByUtilisateurAndEtatIsFalse(utilisateur);
        }

        Publication publication = publicationRepository.findById(idPub).orElse(null);
        Utilisateur UserPub = publicationRepository.findUserPublication(idPub);
        if (publication == null) {
            System.out.println("Publication non trouvée");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        // Vérifier si la publication n'est pas déjà dans le panier
        if (!panier.getPublications().contains(publication) && !publication.isGratuit() && !Objects.equals(UserPub.getEmail(), email)) {
            publication.getPaniers().add(panier);
            panier.setPrixTT(prixAvantAjout+publication.getPrix());
            panierRepository.save(panier);
            publicationRepository.save(publication);
            System.out.println("Publication ajoutée au panier avec succès");
            return ResponseEntity.ok().body(null);
        } else {
            System.out.println("La publication est déjà dans le panier");
            System.out.println("L'utilisateur est le propriétaire de la publication");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/getPublication")
    public ResponseEntity<?> getPublications(@RequestParam("email") String email) {
        Utilisateur utilisateur = utilisateurRepository.findUserByEmail(email);

        if (utilisateur == null) {
            return new ResponseEntity<>("Utilisateur non trouvé", HttpStatus.NOT_FOUND);
        }

        Panier panier = panierRepository.findPanierByUtilisateurAndEtatIsFalse(utilisateur);

        if (panier == null) {
            return new ResponseEntity<>("Panier non trouvé pour cet utilisateur", HttpStatus.NOT_FOUND);
        }

        try {
            Iterable<Publication> publications = publicationRepository.getPublicationByPanier(panier.getIdPanier());

            ResponseEntity<Iterable<Publication>> responseEntity = ResponseEntity.ok().body(publications);
            responseEntity.getHeaders().forEach((headerName, headerValues) ->
                    System.out.println(headerName + ": " + headerValues));

            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la récupération des publications : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/delete/{id}")
    public void deletePublication(
            @PathVariable("id") Long id, @RequestParam("email") String email
    ){
        Utilisateur utilisateur = utilisateurRepository.findUserByEmail(email);

        if (utilisateur == null) {
            System.out.println("utilisateur non trouvé");
        }
        Panier panier = panierRepository.findPanierByUtilisateurAndEtatIsFalse(utilisateur);
        Float prix = panier.getPrixTT();
        Publication publication = publicationRepository.getPublicationById(id);
        publication.getPaniers().remove(panier);
        panier.setPrixTT(prix-publication.getPrix());
        publicationRepository.save(publication);
    }

    @PostMapping("/paiement")
    public void paiementPanier(
            @RequestParam("email") String email
    ) {
        Utilisateur utilisateur = utilisateurRepository.findUserByEmail(email);
        if (utilisateur == null) {
            System.out.println("Utilisateur non trouvé");
        } else {
            Panier panier = panierRepository.findPanierByUtilisateurAndEtatIsFalse(utilisateur);
            if (panier == null) {
                System.out.println("Panier non trouvé");
            } else {
                Set<Publication> publications = panier.getPublications();
                Set<Publication> achatPub = utilisateur.getPublicationsAchats();
                achatPub.addAll(publications);
                panier.setEtat(true);
                userRespository.save(utilisateur);
                panierRepository.save(panier);
            }
        }
    }



}

