package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Login;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.model.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthController {
    private LoginService loginService;
    @PostMapping("/getLogin")
    public Utilisateur getLogin(@RequestBody Utilisateur utilisateur) {
        return loginService.getLogin(utilisateur);
    }
    @PutMapping("/ajoutLogin")
    public Utilisateur addLogin(@RequestBody Utilisateur utilisateur) {
        return loginService.addLoginToDatabase(utilisateur);
    }
}
