package com.example._3dinspire_serveur.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Forme dans laquelle le mail et le mot de passe sont mises dans
 * les requêtes HTTP Rest
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq {
    private String email;
    private String password;
}
