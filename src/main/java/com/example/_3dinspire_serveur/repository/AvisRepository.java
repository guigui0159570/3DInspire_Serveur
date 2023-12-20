package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Avis;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AvisRepository extends CrudRepository<Avis, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Avis avis WHERE avis.publication.id = :id")
    void deleteAvisByPublicationId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Avis avis WHERE avis.utilisateur.id = :id")
    void deleteAvisByUtilisateurId(@Param("id") Long id);

    @Query("select avis from Avis avis where avis.publication = :publication")
    Iterable<Avis> getAvisByPublicationId(Publication publication);
    @Query("select avis from Avis avis where avis.utilisateur = :utilisateur")
    Iterable<Avis> getAvisByUtilisateurId(Utilisateur utilisateur);

    void deleteAvisByUtilisateurId(Long id);
}
