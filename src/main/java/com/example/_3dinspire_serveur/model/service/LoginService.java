package com.example._3dinspire_serveur.model.service;

import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.model.respository.LoginRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final LoginRespository loginRepository;
    @Autowired
    public LoginService(LoginRespository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public int registerNewUser(String email, String password, String pseudo) {
        return loginRepository.registerNewUser(email, pseudo, password);
    }
}
