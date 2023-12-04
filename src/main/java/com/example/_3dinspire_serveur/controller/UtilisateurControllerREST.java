package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Profil;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.PanierRepository;
import com.example._3dinspire_serveur.repository.RoleRepository;
import com.example._3dinspire_serveur.repository.ProfilRepository;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.Iterator;
import java.util.Optional;

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
 // fonction pour s abonner
    @GetMapping("/abonnenement/{user}/{id}")
    public void abonnenement(@PathVariable Long id, @PathVariable Long user){
        Optional<Utilisateur> utilisateur_abonne = utilisateurRepository.findById(id);
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur_abonne.isPresent() && utilisateur.isPresent()){
            if(!id.equals(user)) {
                utilisateur.get().ajouterAbonnement(utilisateur_abonne.get());
                utilisateur_abonne.get().ajouterAbonne(utilisateur.get());
                utilisateurRepository.save(utilisateur.get());
                utilisateurRepository.save(utilisateur_abonne.get());
            }
        }
    }

    // fonction pour se desabonner
    @GetMapping("/desabonnemnt/{user}/{id}")
    public void desabonnenement(@PathVariable Long id, @PathVariable Long user){
        Optional<Utilisateur> utilisateur_abonne = utilisateurRepository.findById(id);
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur_abonne.isPresent() && utilisateur.isPresent()){
            utilisateur.get().deleteAbonnement(utilisateur_abonne.get());
            utilisateur_abonne.get().deleteAbonne(utilisateur.get());
            utilisateurRepository.save(utilisateur_abonne.get());
            utilisateurRepository.save(utilisateur.get());
        }
    }

    // fonction pour avoir un json de mon utilisateur
    @GetMapping("/userInformation/{user}")
    public Map<String, String> userInformation(@PathVariable Long user) {
        Map<String, String> information = new HashMap<>();
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);

        if (utilisateur.isPresent()) {
            Utilisateur userObj = utilisateur.get();

            information.put("idUtilisateur" , String.valueOf(userObj.getId()));
            information.put("countAbonnement", String.valueOf(userObj.countAbonnement()));
            information.put("countAbonne", String.valueOf(userObj.countAbonne()));
            information.put("pseudo", String.valueOf(userObj.getPseudo()));

            Profil profil = userObj.getProfil();
            if (profil != null) {
                information.put("description", profil.getDescription());

                if (profil.getPhoto() != null) {
                    information.put("photo", Base64.getEncoder().encodeToString(profil.getPhoto()));
                } else {
                    information.put("photo", null);
                }
            } else {
                information.put("description", null);
                information.put("photo", null);
            }
        }

        return information;
    }

    // fonction pour avoir une liste de tout les abonnements d'un utilisateur
    @GetMapping("/abonnementUser/{userId}")
    public Set<Map> AbonnementUser(@PathVariable Long userId) {
        Set<Map> abonnementinfo = new HashSet<>();
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(userId);
        if (utilisateurOptional.isPresent()) {
            Utilisateur utilisateur = utilisateurOptional.get();
            for (Utilisateur utilisateur1 : utilisateur.getAbonnements()) {
                abonnementinfo.add(userInformation(utilisateur1.getId()));

            }
            return abonnementinfo;
        } else {
            // L'utilisateur avec l'ID spécifié n'a pas été trouvé
            return Collections.emptySet();
        }
    }

    // fonction pour avoir une liste de tout les abonnes d'un utilisateur

    @GetMapping("/abonneUser/{userId}")
    public Set<Map> AbonneUser(@PathVariable Long userId) {
        Set<Map> abonneinfo = new HashSet<>();
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(userId);
        if (utilisateurOptional.isPresent()) {
            Utilisateur utilisateur = utilisateurOptional.get();
            for (Utilisateur utilisateur1 : utilisateur.getAbonnes()) {
                abonneinfo.add(userInformation(utilisateur1.getId()));

            }
            return abonneinfo;
        } else {
            // L'utilisateur avec l'ID spécifié n'a pas été trouvé
            return Collections.emptySet();
        }
    }


    // fonction pour voir si un abonné est présent dans mes abonnements
    @GetMapping("/presenceAbonne/{userId}/{abonneid}")
    public boolean presenceAbonne(@PathVariable Long userId, @PathVariable Long abonneid) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(userId);
        Optional<Utilisateur> abonneUser = utilisateurRepository.findById(abonneid);

        if (utilisateur.isPresent() && abonneUser.isPresent()) {
            return utilisateur.get().verifAbonnement(abonneUser.get());
        } else {
            return false;
        }
    }

    @GetMapping("/getUtilisateurIdByEmail")
    public Long getUtilisateurIdByEmail(@RequestParam String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);
        return utilisateur.getId();
    }

    @Transactional
    @DeleteMapping("/utilisateur/delete/{id}")
    public void deleteUtilisateur( @PathVariable("id") Long id) {
        try {
            utilisateurRepository.deleteById(id);
            System.out.println("Utilisateur supprimé avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur.");
        }
    }





    // fonction pour enregistrer une photo dans la bdd

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


    // fonction pour enregistrer les string du compte, pseudo, description

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

