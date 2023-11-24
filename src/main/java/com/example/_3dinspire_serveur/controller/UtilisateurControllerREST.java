package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Profil;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.ProfilRepository;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
public class UtilisateurControllerREST {
    private final UtilisateurRepository utilisateurRepository;
    private final ProfilRepository profilRepository;

    public UtilisateurControllerREST(UtilisateurRepository utilisateurRepository, ProfilRepository profilRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.profilRepository = profilRepository;
    }


    @PostMapping("/saveUtilisateur")
    public Utilisateur ajoutUtilisateur(@ModelAttribute@Valid Utilisateur utilisateur, @ModelAttribute@Valid Profil profil){
        profilRepository.save(profil);
        utilisateur.setProfil(profil);
        return utilisateurRepository.save(utilisateur);
    }

    @GetMapping("/abonnenement/{user}/{id}")
    public void abonnenement(@PathVariable Long id, @PathVariable Long user){
        Optional<Utilisateur> utilisateur_abonne = utilisateurRepository.findById(id);
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur_abonne.isPresent() && utilisateur.isPresent()){
            utilisateur.get().ajouterAbonnement(utilisateur_abonne.get());
            utilisateur_abonne.get().ajouterAbonne(utilisateur.get());
            utilisateurRepository.save(utilisateur.get());
            utilisateurRepository.save(utilisateur_abonne.get());
        }
    }

    @GetMapping("/userInformation/{user}")
    public Map<String, String> userInformation(@PathVariable Long user){
        Map<String, String> informaton = new HashMap<>();
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur.isPresent()){
            utilisateur.get().countAbonne();
            informaton.put("countAbonnement", String.valueOf(utilisateur.get().countAbonnement()));
            informaton.put("countAbonne",  String.valueOf(utilisateur.get().countAbonne()));
            if (utilisateur.get().getProfil().getPhoto() != null) {
                informaton.put("photo", Base64.getEncoder().encodeToString((utilisateur.get().getProfil().getPhoto())));
            }else {
                informaton.put("photo", null);
            }
            informaton.put("pseudo", String.valueOf(utilisateur.get().getPseudo()));
            informaton.put("description" , utilisateur.get().getProfil().getDescription());

        }
        return informaton;
    }

    @PostMapping("/upload/{user}")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable Long user) throws IOException {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur.isPresent()){
            if (utilisateur.get().getProfil() != null) {
                Optional<Profil> profil = profilRepository.findById(utilisateur.get().getProfil().getId());
                if (profil.isPresent()){
                    System.out.println(profil.get().getId());
                    profil.get().setPhoto(file.getBytes());
                    utilisateur.get().setProfil(profil.get());
                    profilRepository.save(profil.get());
                }
            }else {
                Profil profil = new Profil(null, file.getBytes(),null);
                utilisateur.get().setProfil(profil);
                profilRepository.save(profil);
            }

        }
        return ResponseEntity.ok("Image téléchargée avec succès !");
    }


    @PostMapping("/updateStringProfil/{user}")
    public ResponseEntity<String> handleFileUpload(
            @RequestBody Map<String, String> requestBody, @PathVariable Long user) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur.isPresent()){
            utilisateur.get().setPseudo(requestBody.get("pseudo").replaceAll("^\"|\"$", ""));
            utilisateurRepository.save(utilisateur.get());
            if (utilisateur.get().getProfil() != null) {
                Optional<Profil> profil = profilRepository.findById(utilisateur.get().getProfil().getId());
                if (profil.isPresent()){
                    profil.get().setDescription(requestBody.get("description").replaceAll("^\"|\"$", ""));
                    profilRepository.save(profil.get());
                }
            }else {
                Profil profil = new Profil(requestBody.get("description"), null,null);
                utilisateur.get().setProfil(profil);
                profilRepository.save(profil);
            }
        }
        return ResponseEntity.ok("OK");
    }
}

