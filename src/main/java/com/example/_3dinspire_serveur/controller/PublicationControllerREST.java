package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Avis;
import com.example._3dinspire_serveur.model.DTO.AvisDTO;
import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.AvisRepository;
import com.example._3dinspire_serveur.repository.PublicationRepository;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/publication")
public class PublicationControllerREST {
    private final PublicationRepository publicationRepository;

    private final UtilisateurRepository utilisateurRepository;
    private final AvisRepository avisRepository;

    @Value("${file.upload-dir-model}")
    private String uploadDirModel;
    @Value("${file.upload-dir-image}")
    private String uploadDirImage;

    public PublicationControllerREST(PublicationRepository publicationRepository, UtilisateurRepository utilisateurRepository, AvisRepository avisRepository) {
        this.publicationRepository = publicationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.avisRepository = avisRepository;
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
    public Iterable<Publication> getAllPublication(){
        return publicationRepository.findAll();
    }

    @PostMapping("/save")
    public Publication savePublication(
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("gratuit") boolean gratuit,
            @RequestParam("publique") boolean publique,
            @RequestParam("prix") float prix,
            @RequestParam("image") MultipartFile image,
            @RequestParam("fichier") MultipartFile fichier,
            @RequestParam("proprietaire") Long proprietaire) {
        // Créer un objet Publication à partir des paramètres
        Publication nouvellePublication = new Publication();
        nouvellePublication.setTitre(titre);
        nouvellePublication.setDescription(description);
        nouvellePublication.setGratuit(gratuit);
        nouvellePublication.setPublique(publique);
        nouvellePublication.setPrix(prix);
        nouvellePublication.setNb_telechargement(0);
        nouvellePublication.setImage("_");
        nouvellePublication.setFichier("_");
        Publication publication = publicationRepository.save(nouvellePublication);
        String lienFichier = "";
        String lienImage = "";
        // Vérifiez si le fichier est vide
        if (fichier.isEmpty()) {
            // Traitement pour un fichier vide, si nécessaire
        } else {
            // Sauvegardez le fichier sur le serveur
            try {
                lienFichier = System.currentTimeMillis() + "_" + publication.getId() + "_" + proprietaire + "_" + titre + "_" + fichier.getOriginalFilename();
                File dest = new File(uploadDirModel + File.separator + lienFichier);
                fichier.transferTo(dest);
                // Vous pouvez également enregistrer le chemin du fichier dans votre base de données si nécessaire.
            } catch (IOException e) {
                e.printStackTrace();
                // Gérer les erreurs liées à l'écriture du fichier
            }
        }
        if (image.isEmpty()) {
        } else {
            try {
                lienImage = System.currentTimeMillis() + "_" + publication.getId() + "_" + proprietaire + "_" + titre + "_" + image.getOriginalFilename();
                File dest = new File(uploadDirImage + File.separator + lienImage);
                image.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        publication.setImage(lienImage);
        publication.setFichier(lienFichier);
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(proprietaire);
        utilisateurOptional.ifPresent(publication::setProprietaire);
        return publicationRepository.save(publication);
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
        publicationOptional.ifPresent(avis::setPublication);

        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(utilisateur_id);
        utilisateurOptional.ifPresent(avis::setUtilisateur);

        return avisRepository.save(avis);
    }
}