package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Panier;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PanierRepository extends CrudRepository<Panier, Long> {

    @Query("select panier.publications from Panier panier where panier.utilisateur.email = :utilisateur")
    Iterable<Publication> getPanier(String utilisateur);

    Panier findPanierByUtilisateur(Utilisateur utilisateur);

    @Query("select panier.prixTT from Panier panier where panier.idPanier = :id")
    Float getPrixByPanier(Long id);



}
