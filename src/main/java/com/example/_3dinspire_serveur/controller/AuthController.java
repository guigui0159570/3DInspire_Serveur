package com.example._3dinspire_serveur.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/mail-password")
    public String emailPassword(){
        return "EmailPassword";
    }
}
