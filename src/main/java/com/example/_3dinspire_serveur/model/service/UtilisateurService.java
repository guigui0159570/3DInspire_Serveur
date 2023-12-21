package com.example._3dinspire_serveur.model.service;

import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.model.DTO.UtilisateurDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UtilisateurService {
    Utilisateur saveUser(UtilisateurDTO userDto);

    Utilisateur findUserByEmail(String email);


    List<UtilisateurDTO> findAllUsers();

    Utilisateur findByResetToken(String resetToken);
}
