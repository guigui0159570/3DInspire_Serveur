package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Profil;
import com.example._3dinspire_serveur.model.Utilisateur;
import org.springframework.data.repository.CrudRepository;

public interface ProfilRepository extends CrudRepository<Profil, Long> {
}
