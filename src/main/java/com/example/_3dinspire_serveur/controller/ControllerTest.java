package com.example._3dinspire_serveur.controller;
import com.example._3dinspire_serveur.model.DTO.UtilisateurDTO;
import com.example._3dinspire_serveur.model.Avis;
import com.example._3dinspire_serveur.model.Profil;
import com.example._3dinspire_serveur.model.Panier;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerTest {

    private final UtilisateurRepository utilisateurRepository;

    public ControllerTest(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping("/form")
    public String form(Model model){
        model.addAttribute("uti", new Utilisateur());
        model.addAttribute("profil", new Profil());
        model.addAttribute("post", new Publication());
        model.addAttribute("avis", new Avis());
        return "form";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new UtilisateurDTO());
        return "register";
    }

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("uti", utilisateurRepository.findAll());
        return "Affichage_User";
    }
}
