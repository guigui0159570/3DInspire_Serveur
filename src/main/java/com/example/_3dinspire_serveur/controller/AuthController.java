package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.UserRespository;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * Controller qui sert à la modification de mot de passe
 */
@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private UtilisateurRepository utilisateurRepository;
    private UserRespository userRespository;

    public AuthController(UtilisateurRepository utilisateurRepository, UserRespository userRespository) {
        this.utilisateurRepository = utilisateurRepository;
        this.userRespository = userRespository;
    }

    /**
     * Page de modification du mot de passe envoyer par mail
     * @param token Token passer dans la requête génerer pour le mail
     * @param model
     * @return Page de modification
     */
    @GetMapping("/mail-password")
    public String emailPassword(@RequestParam String token, Model model) {
        if (isValidToken(token)) {
            model.addAttribute("token", token);
            return "EmailPassword";
        } else {
            return "InvalidToken";
        }
    }

    /**
     * Vérifie si le token existe pour la réinitialisation de mot de passe
     * @param token Token de reinitialisation
     * @return Boolean
     */
    private boolean isValidToken(String token) {
        if (token != null && !token.isEmpty()) {
            return utilisateurRepository.existsByResetToken(token);
        }
        return false;
    }

    /**
     * Mapping qui va changer le mot de passe depuis la page EmailPassword
     * precedement envoyer par mail
     * @param token Token de réinitialisation
     * @param password Nouveau mot de passe
     * @return InvalidToken si pas réussi/Success si réussi
     */
    @PostMapping("/mail-password")
    public String updatePassword(@RequestParam String token, @RequestParam String password) {
        if (isValidToken(token)) {
            Utilisateur user = utilisateurRepository.findByResetToken(token);

            if (user == null) {
                return "InvalidToken";
            }
            System.out.println(password);
            System.out.println(token);
            String encodedPassword = passwordEncoder.encode(password);
            System.out.println(encodedPassword);
            userRespository.UpdatePasswordUtilisateur(encodedPassword, token);

            // Effacez le jeton de réinitialisation après utilisation
            userRespository.UpdateTokenUtilisateur(null,user.getEmail());

            return "PasswordSuccess";
        } else {
            return "InvalidToken";
        }
    }
}
