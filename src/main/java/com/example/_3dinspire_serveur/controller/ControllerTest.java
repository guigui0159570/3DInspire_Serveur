package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.DTO.UtilisateurDTO;
import com.example._3dinspire_serveur.model.Avis;
import com.example._3dinspire_serveur.model.Panier;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerTest {
    @GetMapping("/form")
    public String form(Model model){
        model.addAttribute("uti", new Utilisateur());
        model.addAttribute("post", new Publication());
        model.addAttribute("avis", new Avis());
        return "form";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new UtilisateurDTO());
        return "register";
    }

    @GetMapping("/loginForm")
    public String login(){
        return "loginForm";
    }



}
