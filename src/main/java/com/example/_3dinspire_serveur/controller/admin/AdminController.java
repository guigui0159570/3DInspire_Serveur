package com.example._3dinspire_serveur.controller.admin;

import com.example._3dinspire_serveur.model.Tag;
import com.example._3dinspire_serveur.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
    private PublicationRepository publicationRepository;
    private UtilisateurRepository utilisateurRepository;

    private AvisRepository avisRepository;
    private ProfilRepository profilRepository;
    private TagRespository tagRespository;

    public AdminController(PublicationRepository publicationRepository, UtilisateurRepository utilisateurRepository, AvisRepository avisRepository, ProfilRepository profilRepository, TagRespository tagRespository) {
        this.publicationRepository = publicationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.avisRepository = avisRepository;
        this.profilRepository = profilRepository;
        this.tagRespository = tagRespository;
    }

    /**
     * Page d'accueil pour l'administration
     * @param model Model Spring
     * @return Page "admin"
     */
    @GetMapping("/home")
    public String pageHome(Model model){
        model.addAttribute("users", utilisateurRepository.findAll());
        model.addAttribute("publications", publicationRepository.findAll());
        return "admin";
    }

    /**
     * Suppression d'un Utilisateur
     * @param id ID de l'Utilisateur
     * @return ResponseEntity
     */
    @Transactional
    @DeleteMapping("/utilisateur/delete/{id}")
    public ResponseEntity<String> deleteUtilisateur( @PathVariable("id") Long id) {
        System.out.println("Ta mere");
        try {
            avisRepository.deleteAvisByUtilisateurId(id);
            publicationRepository.deletePublicationByProprietaire(utilisateurRepository.findById(id).get());
            profilRepository.deleteProfilByUtilisateurId(id);
            utilisateurRepository.deleteById(id);
            System.out.println("Utilisateur supprimé avec succès.");
            return ResponseEntity.ok("Utilisateur supprimé avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur.");
        }
        return null;
    }

    /**
     * Suppression de Publication
     * @param id ID de la Publication
     * @return ResponseEntity
     */
    @Transactional
    @DeleteMapping("/publication/delete/{id}")
    public ResponseEntity<String> deletePublication(@PathVariable("id") Long id) {
        try {
            avisRepository.deleteAvisByPublicationId(id);
            publicationRepository.deleteById(id);
            System.out.println("Publication supprimé avec succès.");
            return ResponseEntity.ok("Publication supprimé avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la publication.");
        }
        return null;
    }
}
