package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.DTO.UtilisateurDTO;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@Controller
public class ControllerTest {
    @GetMapping("/form")
    public String form(Model model){
        model.addAttribute("uti", new Utilisateur());
        model.addAttribute("post", new Publication());
        return "form";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new UtilisateurDTO());
        return "register";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }



}
