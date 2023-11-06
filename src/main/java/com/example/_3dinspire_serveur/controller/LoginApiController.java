package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.model.respository.LoginRespository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginApiController {
   /* private LoginService loginService;
    private LoginRespository loginRespository;



    @PostMapping("/getLogin")
    public Utilisateur getLogin(@RequestBody Utilisateur utilisateur) {
        return loginService.getLogin(utilisateur);
    }
    @PutMapping("/ajoutLogin")
    public Utilisateur addLogin(@RequestBody Utilisateur utilisateur) {
        return loginService.addLoginToDatabase(utilisateur);
    }
    @PostMapping("/login")
    public Utilisateur createUser(@RequestParam("email")String email,
                                     @RequestParam("pseudo")String pseudo,
                                     @RequestParam("password")String password){
        String hashed_password = BCrypt.hashpw(password, BCrypt.gensalt());
        Utilisateur user = new Utilisateur(email,pseudo,hashed_password);
         res = loginRespository.save(user);
        return res;
    }*/
}
