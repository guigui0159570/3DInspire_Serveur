package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Avis;
import com.example._3dinspire_serveur.model.DTO.AvisDTO;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Tag;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.AvisRepository;
import com.example._3dinspire_serveur.repository.PublicationRepository;
import com.example._3dinspire_serveur.repository.TagRespository;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/publication")
public class PublicationControllerREST {
    private final PublicationRepository publicationRepository;

    private final UtilisateurRepository utilisateurRepository;
    private final AvisRepository avisRepository;
    private final TagRespository tagRespository;

    @Value("${file.upload-dir-model}")
    private String uploadDirModel;
    @Value("${file.upload-dir-image}")
    private String uploadDirImage;

    public PublicationControllerREST(PublicationRepository publicationRepository, UtilisateurRepository utilisateurRepository, AvisRepository avisRepository, TagRespository tagRespository) {
        this.publicationRepository = publicationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.avisRepository = avisRepository;
        this.tagRespository = tagRespository;
    }

    @GetMapping("/get/{id}")
    public Publication getPublication(
            @PathVariable("id") Long id
    ){
        if (publicationRepository.findById(id).isPresent())
        return publicationRepository.findById(id).get();
        else return null;
    }

    @GetMapping("/get/uti/{id}")
    public Iterable<Publication> getPublicationByUtilisateurId(
            @PathVariable("id") Long id
    ){
        if (utilisateurRepository.findById(id).isPresent())
            return publicationRepository.getPublicationByProprietaireId(utilisateurRepository.findById(id).get());
        else return null;
    }


    @GetMapping("/getAll")
    public ResponseEntity<Iterable<Publication>> getAllPublication() {
        Iterable<Publication> publications = publicationRepository.findAll();
        ResponseEntity<Iterable<Publication>> responseEntity = ResponseEntity.ok().body(publications);
        responseEntity.getHeaders().forEach((headerName, headerValues) ->
                System.out.println(headerName + ": " + headerValues));

        return responseEntity;
    }

    @GetMapping("/getAllByTime")
    public ResponseEntity<Iterable<Publication>> getAllPublicationByTime() {
        Iterable<Publication> publications = publicationRepository.getPublicationByTime();
        ResponseEntity<Iterable<Publication>> responseEntity = ResponseEntity.ok().body(publications);
        responseEntity.getHeaders().forEach((headerName, headerValues) ->
                System.out.println(headerName + ": " + headerValues));

        return responseEntity;
    }

    @PostMapping("/save")
    public Publication savePublication(
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("gratuit") boolean gratuit,
            @RequestParam("publique") boolean publique,
            @RequestParam("prix") float prix,
            @RequestParam("image") MultipartFile image,
            @RequestParam("tags") List<String> tags,
            @RequestParam("email") String email
            ) {

        // Créer un objet Publication à partir des paramètres
        Publication nouvellePublication = new Publication();
        nouvellePublication.setTitre(titre);
        nouvellePublication.setDescription(description);
        nouvellePublication.setGratuit(gratuit);
        nouvellePublication.setPublique(publique);
        nouvellePublication.setPrix(prix);
        nouvellePublication.setNb_telechargement(0);
        nouvellePublication.setFichier("_");
        nouvellePublication.setImage("_");
        nouvellePublication.setDateLocal(LocalDateTime.now());
        Publication publication = publicationRepository.save(nouvellePublication);
        String nouvemail = email.replaceAll("\"", "");

        Optional<Utilisateur> utilisateurOptional = Optional.ofNullable(utilisateurRepository.findByEmail(nouvemail));
        Utilisateur utilisateur = utilisateurOptional.orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé pour l'e-mail " + email));

        publication.setImage(isEmpty(image, publication.getId(), utilisateur.getId(), titre, "i"));

        for (String tagNom : tags) {
            String tagNomSansGuillemets = tagNom.replaceAll("\"", "");  // Enlever les guillemets
            Optional<Tag> tagOptional = tagRespository.findByNom(tagNomSansGuillemets);

            Tag tag = tagOptional.orElseGet(() -> {
                Tag nouveauTag = new Tag();
                nouveauTag.setNom(tagNomSansGuillemets);
                return tagRespository.save(nouveauTag);
            });

            // Associer la publication au tag et vice versa
            publication.getTags().add(tag);
            tag.getPublications().add(publication);
        }

        utilisateurOptional.ifPresent(publication::setProprietaire);
        return publicationRepository.save(publication);
    }

    public String isEmpty(MultipartFile object, long publication_id, long proprietaire_id, String titre, String type){
        if (object.isEmpty()) {
            System.out.println("Fichier est vide");
        } else {
            try {
                String lien = System.currentTimeMillis() + "_" + publication_id + "_" + proprietaire_id + "_" + titre + "_" + object.getOriginalFilename();
                File dest;
                if (Objects.equals(type, "i")){
                    dest = new File(uploadDirImage + File.separator + lien);
                } else {
                    dest = new File(uploadDirModel + File.separator + lien);
                }
                object.transferTo(dest);
                return lien;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "_";
    }

    @DeleteMapping("/delete/{id}")
    public void deletePublication(
            @PathVariable("id") Long id
    ){
        publicationRepository.deleteById(id);
        avisRepository.deleteAvisByPublicationId(id);
    }

    @GetMapping("/avis/get/pub/{id}")
    public Iterable<AvisDTO> getAllAvisByPublication(
            @PathVariable("id") Long id
    ){
        Iterable<Avis> avisList = avisRepository.getAvisByPublicationId(publicationRepository.findById(id).get());
        List<AvisDTO> avisDTOList = new ArrayList<>();

        for (Avis avis : avisList) {
            avisDTOList.add(new AvisDTO(avis.getId(),
                    avis.getCommentaire(),
                    avis.getEtoile(),
                    avis.getPublication().getId(),
                    avis.getUtilisateur().getId()));
        }
        return avisDTOList;
    }

    @GetMapping("/avis/get/uti/{id}")
    public Iterable<AvisDTO> getAllAvisByUtilisateur(
            @PathVariable("id") Long id
    ){
        Iterable<Avis> avisList = avisRepository.getAvisByUtilisateurId(utilisateurRepository.findById(id).get());
        List<AvisDTO> avisDTOList = new ArrayList<>();

        for (Avis avis : avisList) {
            avisDTOList.add(new AvisDTO(avis.getId(),
                    avis.getCommentaire(),
                    avis.getEtoile(),
                    avis.getPublication().getId(),
                    avis.getUtilisateur().getId()));
        }
        return avisDTOList;
    }

    @GetMapping("/avis/get")
    public Iterable<AvisDTO> getAllAvis() {
        Iterable<Avis> avisList = avisRepository.findAll();
        List<AvisDTO> avisDTOList = new ArrayList<>();

        for (Avis avis : avisList) {
            avisDTOList.add(new AvisDTO(avis.getId(),
                    avis.getCommentaire(),
                    avis.getEtoile(),
                    avis.getPublication().getId(),
                    avis.getUtilisateur().getId()));
        }

        return avisDTOList;
    }

    @PostMapping("/avis/save")
    public Avis saveAvis(
            @RequestParam("commentaire") String commentaire,
            @RequestParam("etoile") int etoile,
            @RequestParam("publication") long publication_id,
            @RequestParam("utilisateur") long utilisateur_id
    ) {
        Avis avis = new Avis();
        avis.setCommentaire(commentaire);
        avis.setEtoile(etoile);

        Optional<Publication> publicationOptional = publicationRepository.findById(publication_id);
        publicationOptional.ifPresentOrElse(
                avis::setPublication, // Consumer for if the Optional is present
                new Runnable() { // Runnable for if the Optional is empty
                    @Override
                    public void run() {
                        // Handle the case where publicationOptional is not present
                        System.err.println("Publication not found for ID: " + publication_id);
                        new Throwable().printStackTrace(); // Print the stack trace
                    }
                }
        );
        
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(utilisateur_id);
        utilisateurOptional.ifPresent(avis::setUtilisateur);
        utilisateurOptional.ifPresentOrElse(
                avis::setUtilisateur,
                new Runnable() {
                    @Override
                    public void run() {
                        // Handle the case where publicationOptional is not present
                        System.err.println("Publication not found for ID: " + utilisateur_id);
                        new Throwable().printStackTrace(); // Print the stack trace
                    }
                }
        );

        return avisRepository.save(avis);
    }
}