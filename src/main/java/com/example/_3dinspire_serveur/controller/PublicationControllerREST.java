package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

@RestController
public class PublicationControllerREST {
    private PublicationRepository publicationRepository;

    @Value("${file.upload-dir-model}")
    private String uploadDirModel;
    @Value("${file.upload-dir-image}")
    private String uploadDirImage;

    public PublicationControllerREST(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    @GetMapping("/getAllPublication")
    public Iterable<Publication> getAllPublication(){
        return publicationRepository.findAll();
    }

    @PostMapping("/savePublication")
    public Publication savePublication(
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam("gratuit") boolean gratuit,
            @RequestParam("publique") boolean publique,
            @RequestParam("prix") float prix,
            @RequestParam("image") MultipartFile image,
            @RequestParam("fichier") MultipartFile fichier,
            @RequestParam("proprietaire") Long proprietaire) {

        String lienFichier = "";
        String lienImage = "";
        // Vérifiez si le fichier est vide
        if (fichier.isEmpty()) {
            // Traitement pour un fichier vide, si nécessaire
        } else {
            // Sauvegardez le fichier sur le serveur
            try {
                lienFichier = System.currentTimeMillis() + "_" + proprietaire.toString() + "_" + titre + "_" + fichier.getOriginalFilename();
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
                lienImage = System.currentTimeMillis() + "_" + proprietaire + "_" + titre + "_" + image.getOriginalFilename();
                File dest = new File(uploadDirImage + File.separator + lienImage);
                image.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Créer un objet Publication à partir des paramètres
        Publication nouvellePublication = new Publication();
        nouvellePublication.setTitre(titre);
        nouvellePublication.setDescription(description);
        nouvellePublication.setGratuit(gratuit);
        nouvellePublication.setPublique(publique);
        nouvellePublication.setPrix(prix);
        nouvellePublication.setImage(lienImage);
        nouvellePublication.setFichier(lienFichier);
        nouvellePublication.setNb_telechargement(0);
        return publicationRepository.save(nouvellePublication);
    }
}
