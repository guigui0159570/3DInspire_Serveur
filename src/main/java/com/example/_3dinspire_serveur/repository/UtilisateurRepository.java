package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Utilisateur;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {
    Utilisateur findByEmail(String email);
}
