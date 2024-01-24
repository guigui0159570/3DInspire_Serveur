package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Publication;
import com.example._3dinspire_serveur.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controlleur pour gérer les images et fichiers 3D enregistrer sur le serveur
 */
@RestController
@RequestMapping("/fichiers")
public class FichiersController {
    private final PublicationRepository publicationRepository;

    public FichiersController(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    @Value("${file.upload-dir-model}")
    private String uploadDirModel;

    @Value("${file.upload-dir-image}")
    private String uploadDirImage;

    @Value("${file.upload-dir-image-profil}")
    private String uploadDirImageProfil;

    /**
     * Renvoie un RessourceEntity avec un fichier 3D
     * @param nomFichier nom du fichier
     * @return ResponseEntity<Resource> Ressource étant un fichier
     * @throws MalformedURLException
     */
    @GetMapping("/model/{nomFichier:.+}")
    public ResponseEntity<Resource> getModel(@PathVariable String nomFichier) throws MalformedURLException {
        Pattern pattern = Pattern.compile("_(\\d+)_");
        Matcher matcher = pattern.matcher(nomFichier);

        // Vérifier si le motif correspondant est trouvé
        if (matcher.find()) {
            String extractedNumber = matcher.group(1);
            long number = Long.parseLong(extractedNumber);
            System.out.println("Numéro extrait : " + number);
            Optional<Publication> publication = publicationRepository.findById(number);
            publication.ifPresent(value -> value.setNb_telechargement(value.getNb_telechargement() + 1));
            publication.ifPresent(publicationRepository::save);
        }
        return getResourceResponseEntity(nomFichier, uploadDirModel);
    }

    /**
     * Renvoie un RessourceEntity avec une image
     * @param nomFichier nom de l'image
     * @return ResponseEntity<Resource> Ressource étant une image
     * @throws MalformedURLException
     */
    @GetMapping("/image/{nomFichier:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String nomFichier) throws MalformedURLException {
        return getResourceResponseEntity(nomFichier, uploadDirImage);
    }

    /**
     * Va cherhce le fichier en fonction de son nom et de son répertoire
     * @param nomFichier Nom du fichier
     * @param uploadDir Répertoire du fichier
     * @return ResponseEntity avec le fichier
     * @throws MalformedURLException
     */
    private ResponseEntity<Resource> getResourceResponseEntity(@PathVariable String nomFichier, String uploadDir) throws MalformedURLException {
        Path filePath = Paths.get(uploadDir).resolve(nomFichier).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            // Gérer l'erreur si le fichier n'est pas trouvé
            return ResponseEntity.notFound().build();
        }
    }
}
