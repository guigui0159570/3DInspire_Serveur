package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Publication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationRepository extends CrudRepository<Publication, Long> {

}
