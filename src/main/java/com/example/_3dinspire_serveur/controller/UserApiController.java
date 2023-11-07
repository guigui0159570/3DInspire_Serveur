package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.model.respository.LoginRespository;
import com.example._3dinspire_serveur.model.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class UserApiController {

    LoginService loginService;
    LoginRespository loginRespository;


    public UserApiController(LoginService loginService, LoginRespository loginRespository) {
        this.loginService = loginService;
        this.loginRespository = loginRespository;
    }

    @PostMapping("/user/register")
    public ResponseEntity registerNewUser(@RequestParam("email") String email,
                                          @RequestParam("password") String password,
                                          @RequestParam("pseudo") String pseudo) {

        if (email.isEmpty() || password.isEmpty() || pseudo.isEmpty()) {
            return new ResponseEntity<>("Remplir tous les champs", HttpStatus.BAD_REQUEST);
        }
        // Encrypt / Hash mot de passe
        String hashed_password = BCrypt.hashpw(password, BCrypt.gensalt());


        // Enregistrer nouv user
        int res = loginService.registerNewUser(email, pseudo, hashed_password);

        if (res != 1) {
            return new ResponseEntity<>("Erreur dans enregistrement", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Succes de l'enregistrement", HttpStatus.OK);

    }



    @PostMapping("/user/registerV2")
    public Utilisateur registerNewUser2(@RequestParam(value = "email") String email,
                                          @RequestParam(value = "password") String password,
                                          @RequestParam(value = "pseudo") String pseudo){
        String hashed_password = BCrypt.hashpw(password, BCrypt.gensalt(12));

        Utilisateur uti = new Utilisateur(email,pseudo,hashed_password);
        return loginRespository.save(uti);
    }

    @GetMapping("/test")
    public String testEndpoint(){
        return "connecte";
    }
}
