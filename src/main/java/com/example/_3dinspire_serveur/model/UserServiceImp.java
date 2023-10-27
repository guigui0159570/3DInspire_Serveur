package com.example._3dinspire_serveur.model;


import com.example._3dinspire_serveur.model.respository.LoginRespository;
import com.example._3dinspire_serveur.model.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.persistence.EntityManager;
@Service
public class UserServiceImp implements LoginService {

    private final EntityManager entityManager;

    @Autowired
    private LoginRespository loginRespository;

    @Autowired
    public UserServiceImp(EntityManager theEntityManager) {
        this.entityManager = theEntityManager;
    }

    @Override
    public Utilisateur getLogin(Utilisateur userDetails) {
        try {
            TypedQuery<Utilisateur> typedQuery = entityManager.createQuery(
                    "FROM Utilisateur WHERE password=:password AND email=:email", Utilisateur.class);
            typedQuery.setParameter("password", userDetails.getPassword())
                    .setParameter("email", userDetails.getEmail());
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Utilisateur addLoginToDatabase(Utilisateur utilisateur) {
        return loginRespository.save(utilisateur);
    }
}
