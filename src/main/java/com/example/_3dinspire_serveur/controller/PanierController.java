package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Panier;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.PanierRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanierController {
    
    @GetMapping("/pagePanier")
    public String pagePanier(Model model){
        model.addAttribute("panier", new Panier());
        return "panier";
    }


    @GetMapping("/searchPanier")
    public String searchPanier(Model model){
        model.addAttribute("utilisateur", new Utilisateur());
        return "searchPanier";
    }
}
