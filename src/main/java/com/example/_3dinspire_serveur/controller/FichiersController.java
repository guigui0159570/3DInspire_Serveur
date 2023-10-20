package com.example._3dinspire_serveur.controller;

import org.springframework.beans.factory.annotation.Value;
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

@RestController
@RequestMapping("/fichiers")
public class FichiersController {

    @Value("${file.upload-dir-model}")
    private String uploadDirModel;
    @Value("${file.upload-dir-image}")
    private String uploadDirImage;
    @GetMapping("/model/{nomFichier:.+}")
    public ResponseEntity<Resource> getModel(@PathVariable String nomFichier) throws MalformedURLException {
        return getResourceResponseEntity(nomFichier, uploadDirModel);
    }

    @GetMapping("/image/{nomFichier:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String nomFichier) throws MalformedURLException {
        return getResourceResponseEntity(nomFichier, uploadDirImage);
    }

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
