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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private UtilisateurService userService;

    public AuthController(UtilisateurService utilisateurService) {
        this.userService = utilisateurService;
    }
    @PostMapping("/register/save")
    public ResponseEntity<Utilisateur> registration(@Valid @ModelAttribute("user") UtilisateurDTO userDto,
                                       BindingResult result,
                                       Model model){
        Utilisateur existingUser = userService.findUserByEmail(userDto.getEmail());
        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "Un compte existe déjà sous cette adresse email.");
        }

        if(result.hasErrors()){
            // model.addAttribute("user", userDto);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Utilisateur u = userService.saveUser(userDto);
        return ResponseEntity.ok(u);
    }
}
