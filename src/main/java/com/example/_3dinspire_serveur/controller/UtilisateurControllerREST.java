package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class UtilisateurControllerREST {
    private UtilisateurRepository utilisateurRepository;

    public UtilisateurControllerREST(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @PostMapping("/saveUtilisateur")
    public Utilisateur saveUtilisateur(@ModelAttribute@Valid Utilisateur utilisateur){
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
}
