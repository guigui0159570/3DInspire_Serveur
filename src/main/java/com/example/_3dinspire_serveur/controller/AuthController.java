package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.DTO.UtilisateurDTO;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.model.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private UtilisateurService userService;

    public AuthController(UtilisateurService utilisateurService) {
        this.userService = utilisateurService;
    }
    @PostMapping("/register/save")
    public ResponseEntity<Utilisateur> registration(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("pseudo") String pseudo){
        UtilisateurDTO userDto = new UtilisateurDTO(email,pseudo ,password);
        System.out.println("Onscription : " + email + " / " + password + " / " + pseudo);
        Utilisateur existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            System.out.println("BUG !!!!!!!!!!!!!!!!!!!!!!!!");
        }

        Utilisateur u = userService.saveUser(userDto);
        return ResponseEntity.ok(u);
    }
}
