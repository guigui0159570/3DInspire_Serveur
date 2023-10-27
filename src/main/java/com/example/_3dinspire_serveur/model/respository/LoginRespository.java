package com.example._3dinspire_serveur.model.respository;

import com.example._3dinspire_serveur.model.Login;
import com.example._3dinspire_serveur.model.Utilisateur;
import org.springframework.data.repository.CrudRepository;

public interface LoginRespository extends CrudRepository<Utilisateur,String> {
}
