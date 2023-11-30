package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PublicationRepository extends CrudRepository<Publication, Long> {
    @Query("select publication from Publication publication where publication.proprietaire = :utilisateur order by publication.dateLocal desc")
    Iterable<Publication> getPublicationByProprietaireId(Utilisateur utilisateur);


    @Query("select publication from Publication publication where publication.gratuit = :gratuit " +
            "and publication.proprietaire = :proprio")
    Iterable<Publication> getPublicationByStatut(Boolean gratuit, Long proprio);


    @Query("select publication from Publication publication order by publication.dateLocal desc ")
    Iterable<Publication> getPublicationByTime();
}
