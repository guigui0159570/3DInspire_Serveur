package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilisateurControllerREST {
    @PostMapping("/ajoutUtilisateur")
    public Utilisateur ajoutUtilisateur(@ModelAttribute@Valid Utilisateur utilisateur){
        return new Utilisateur();
    }
}
