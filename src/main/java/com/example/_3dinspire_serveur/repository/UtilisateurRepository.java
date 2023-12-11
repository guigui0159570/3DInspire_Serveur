package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {

    Utilisateur findByEmail(String nouvemail);
    Utilisateur findByResetToken(String resetToken);

    boolean existsByResetToken(String resetToken);
}
