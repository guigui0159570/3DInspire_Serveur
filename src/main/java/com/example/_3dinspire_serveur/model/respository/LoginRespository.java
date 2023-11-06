package com.example._3dinspire_serveur.model.respository;

import com.example._3dinspire_serveur.model.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LoginRespository extends CrudRepository<Utilisateur,Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO utilisateur (email, pseudo, password) VALUES (:email,:pseudo, :password,)", nativeQuery = true)
    int registerNewUser(@Param("email") String email, @Param("pseudo") String pseudo, @Param("password") String password);
}
