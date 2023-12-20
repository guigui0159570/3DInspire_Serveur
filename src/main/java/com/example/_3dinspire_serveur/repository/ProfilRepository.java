package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Profil;
import com.example._3dinspire_serveur.model.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProfilRepository extends CrudRepository<Profil, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Profil p WHERE p.utilisateur.id = :id")
    void deleteProfilByUtilisateurId(@Param("id") Long id);

}
