package com.example._3dinspire_serveur.model.service;

import com.example._3dinspire_serveur.model.Login;
import com.example._3dinspire_serveur.model.Utilisateur;

public interface LoginService {
    public Utilisateur getLogin(Utilisateur utilisateur);
    public Utilisateur addLoginToDatabase(Utilisateur utilisateur);
}
