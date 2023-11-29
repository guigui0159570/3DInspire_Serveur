package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Panier;
import com.example._3dinspire_serveur.model.Utilisateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PanierRepository extends CrudRepository<Panier, Long> {

    @Query("select panier from Panier panier where panier.proprietaire.email = :utilisateur")
    Panier findPanierByUser(String utilisateur);

    @Query("select panier from Panier panier where panier.etat = :etat")
    Iterable<Panier> findPanierByEtat(Boolean etat);

}
