package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRespository extends CrudRepository<Utilisateur, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE Utilisateur u set u.resetToken = :token WHERE u.email = :email")
    void UpdateTokenUtilisateur(@Param("token") String token,@Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Utilisateur u set u.password = :password WHERE u.resetToken = :token")
    void UpdatePasswordUtilisateur(@Param("password") String password,@Param("token") String token);

}
