package com.example._3dinspire_serveur;

import com.example._3dinspire_serveur.model.Avis;
import com.example._3dinspire_serveur.model.DTO.UtilisateurDTO;
import com.example._3dinspire_serveur.model.Panier;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.AvisRepository;
import com.example._3dinspire_serveur.repository.PublicationRepository;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private AvisRepository avisRepository;

    @Test
    public void ajoutUtilisateur() throws Exception{
        this.entityManager.persist(new Utilisateur("sboot@gmail.com", "1234", "test"));
        Utilisateur user = this.utilisateurRepository.findByEmail("sboot@gmail.com");
        assertThat(user.getEmail()).isEqualTo("sboot@gmail.com");
        assertThat(user.getPseudo()).isEqualTo("1234");
    }

    @Test
    public void publicationTest() throws Exception{
        this.entityManager.persist(new Utilisateur("publication@gmail.com", "1234", "test"));
        Utilisateur user = this.utilisateurRepository.findByEmail("publication@gmail.com");
        assertThat(user.getEmail()).isEqualTo("publication@gmail.com");
        assertThat(user.getPseudo()).isEqualTo("1234");

        this.entityManager.persist(new Publication("Test", "test de la pub", true, true, 0, "image", "fichier", 0, user, LocalDateTime.now()));
        Publication publication = this.publicationRepository.getDistinctFirstByProprietaireId(user.getId());
        assertThat(publication.getTitre()).isEqualTo("Test");

        this.entityManager.persist(new Avis("Ceci est un avis", 3, publication, publication.getProprietaire()));
        Avis avis = this.avisRepository.getAvisByPublicationId(publication).iterator().next();
        assertThat(avis.getCommentaire()).isEqualTo("Ceci est un avis");
        this.entityManager.persist(new Avis("Ceci est un avis plutot positif", 3, publication, publication.getProprietaire()));
        Avis avis1 = this.avisRepository.getAvisByPublicationId(publication).iterator().next();
        assertThat(avis1.getCommentaire()).isEqualTo("Ceci est un avis");

        publication = this.publicationRepository.findById(publication.getId()).get();
    }
}
