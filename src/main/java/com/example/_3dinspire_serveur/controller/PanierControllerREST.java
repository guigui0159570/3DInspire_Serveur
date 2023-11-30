//package com.example._3dinspire_serveur.controller;
//
//import com.example._3dinspire_serveur.repository.PublicationRepository;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Controller
//public class PanierControllerREST {
//    private final PublicationRepository publicationRepository;
//
//    public PanierControllerREST(PublicationRepository publicationRepository) {
//        this.publicationRepository = publicationRepository;
//    }
//
//    @GetMapping("/affichePanier")
//    public String afficherPanier(Model model){
//        model.addAttribute("publications",publicationRepository.findAll());
//        return "panier";
//    }
//}
