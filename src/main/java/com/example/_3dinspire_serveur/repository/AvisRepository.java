package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Avis;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AvisRepository extends CrudRepository<Avis, Long> {
    @Query("delete Avis avis where avis.publication = :id")
    void deleteAvisByPublicationId(long id);

    @Query("select avis from Avis avis where avis.publication = :publication")
    Iterable<Avis> getAvisByPublicationId(Publication publication);
    @Query("select avis from Avis avis where avis.utilisateur = :utilisateur")
    Iterable<Avis> getAvisByUtilisateurId(Utilisateur utilisateur);
}
