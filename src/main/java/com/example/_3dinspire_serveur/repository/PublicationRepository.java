package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Panier;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PublicationRepository extends CrudRepository<Publication, Long> {

    @Query("select publication from Publication publication where publication.proprietaire = :utilisateur order by publication.dateLocal desc")
    Iterable<Publication> getPublicationByProprietaireId(Utilisateur utilisateur);


    @Query("select publication from Publication publication where publication.gratuit = :gratuit " +
            "and publication.proprietaire = :proprio")
    Iterable<Publication> getPublicationByStatut(Boolean gratuit, Long proprio);


    @Query("select publication from Publication publication where publication.publique = true order by publication.dateLocal desc ")
    Iterable<Publication> getPublicationByTime();

    @Query("SELECT publication FROM Publication publication " +
            "LEFT JOIN publication.tags tags " +
            "WHERE " +
            "publication.proprietaire.pseudo LIKE %:filtre% OR " +
            "publication.proprietaire.email LIKE %:filtre% OR " +
            "publication.titre LIKE %:filtre% OR " +
            "tags.nom LIKE %:filtre%")
    Iterable<Publication> getFiltre(@Param("filtre") String filtre);

    @Query("SELECT DISTINCT publication FROM Publication publication " +
            "LEFT JOIN publication.tags tags " +
            "WHERE " +
            "(publication.proprietaire.id = :id) AND " +
            "(publication.titre LIKE %:filtre% OR tags.nom LIKE %:filtre%)")
    Iterable<Publication> getFiltreByUser(@Param("filtre") String filtre, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Publication p WHERE p.proprietaire.id = :id")
    void deletePublicationByProprietaire(@Param("id") Long id);

    @Query("SELECT DISTINCT publication FROM Publication publication " +
            "JOIN publication.paniers panier " +
            "WHERE panier.idPanier = :id")
    Iterable<Publication> getPublicationByPanier(@Param("id") Long id);


    Publication getPublicationById (@Param("id") Long id);
    @Query("SELECT publication.proprietaire FROM Publication publication WHERE publication.id = :id")
    Utilisateur findUserPublication(@Param("id") Long id);

}
