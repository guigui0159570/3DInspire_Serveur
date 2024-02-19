package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.Notification;
import com.example._3dinspire_serveur.model.Profil;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Not;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UtilisateurControllerREST {

    @Value("${file.upload-dir-image-profil}")
    private String uploadDirImageProfil;
    private final UtilisateurRepository utilisateurRepository;
    private final ProfilRepository profilRepository;
    private final PublicationRepository publicationRepository;
    private final NotificationRepository notificationRepository;
    private final AvisRepository avisRepository;

    public UtilisateurControllerREST(UtilisateurRepository utilisateurRepository, ProfilRepository profilRepository, PublicationRepository publicationRepository, NotificationRepository notificationRepository, AvisRepository avisRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.profilRepository = profilRepository;
        this.publicationRepository = publicationRepository;
        this.notificationRepository = notificationRepository;
        this.avisRepository = avisRepository;
    }

    /**
     * Ajout d'un utilisateur
     * @param utilisateur Utilisateur DTO
     * @param profil Profil de l'Utilisateur
     * @return Utilisateur
     */
    @PostMapping("/saveUtilisateur")
    public Utilisateur ajoutUtilisateur(@ModelAttribute@Valid Utilisateur utilisateur, @ModelAttribute@Valid Profil profil){
        profilRepository.save(profil);
        utilisateur.setProfil(profil);
        return utilisateurRepository.save(utilisateur);
    }

    /**
     * S'abonner à un Utilisateur
     * @param id ID de l'Utilisateur à qui l'on s'abonne
     * @param user ID de l'Utilisateur qui s'abonne
     */
    @GetMapping("/abonnenement/{user}/{id}")
    public void abonnenement(@PathVariable Long id, @PathVariable Long user){
        Optional<Utilisateur> utilisateur_abonne = utilisateurRepository.findById(id);
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur_abonne.isPresent() && utilisateur.isPresent()){
            if(!id.equals(user)) {
                Notification notification = new Notification(utilisateur.get().getPseudo() + " c'est abonné à vous", LocalDate.now(),null, utilisateur.get().getId());
                utilisateur.get().ajouterAbonnement(utilisateur_abonne.get());
                utilisateur_abonne.get().ajouterAbonne(utilisateur.get());
                System.out.println(notification.getId());
                utilisateur_abonne.get().ajouterNotification(notification);
                notification.setUtilisateur(utilisateur_abonne.get());

                notificationRepository.save(notification);
                utilisateurRepository.save(utilisateur.get());
                utilisateurRepository.save(utilisateur_abonne.get());
            }
        }
    }

    /**
     * Se désabonner d'un Utilisateur
     * @param id ID de l'Utilisateur à qui l'on se désabonne
     * @param user ID de l'Utilisateur qui se désabonne
     */
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

    /**
     * Fonction pour envoyer une notification à tout ce qui veulent etre notifié de utilisateur en paramètre
     * @param user ID de l'Utilisateur
     * @return
     */
    @GetMapping("/allsendnotification/{user}")
    public String allsendnotification(@PathVariable Long user){
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur.isPresent()){
            for (Utilisateur utilisateurnotifie : utilisateur.get().getUtilisateursNotifies()){
                System.out.println(utilisateurnotifie.getPseudo());
                Notification notification = new Notification("Nouvelle publication de "+utilisateur.get().getPseudo(), LocalDate.now(), utilisateurnotifie,utilisateur.get().getId());
                utilisateurnotifie.ajouterNotification(notification);
                notificationRepository.save(notification);
                utilisateurRepository.save(utilisateurnotifie);
            }
        }
        return "ok";
    }

    /**
     * Fonction pour se notifie
     * @param id ID de l'Utilisateur pour qui l'on active la cloche
     * @param user ID de l'Utilisateur qui active la cloche
     */
    @GetMapping("/senotifie/{user}/{id}")
    public void senotifie(@PathVariable Long id, @PathVariable Long user){
        Optional<Utilisateur> utilisateur_abonnement = utilisateurRepository.findById(id);
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur_abonnement.isPresent() && utilisateur.isPresent()){
            if(!id.equals(user)) {
                utilisateur_abonnement.get().ajouterUserNotifie(utilisateur.get());
                utilisateurRepository.save(utilisateur_abonnement.get());
            }
        }
    }

    /**
     * Fonction pour enlever la notification
     * @param id ID de l'Utilisateur pour qui l'on désactive la cloche
     * @param user ID de l'Utilisateur qui désactive la cloche
     */
    @GetMapping("/sedenotifie/{user}/{id}")
    public void sedenotifie(@PathVariable Long id, @PathVariable Long user){
        Optional<Utilisateur> utilisateur_abonne = utilisateurRepository.findById(id);
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur_abonne.isPresent() && utilisateur.isPresent()){
            utilisateur_abonne.get().deleteUserNotifie(utilisateur.get());
            utilisateurRepository.save(utilisateur_abonne.get());
        }
    }

    /**
     * Retourne les notifications en attentes de l'Utilisateur
     * @param user ID de l'Utilisateur
     * @return Iterable Map <nom, donnée>
     */
    @GetMapping("/notificationUtilisateur/{user}")
    public Set<Map<String,String>> notificationUtilisateur(@PathVariable Long user){
        Set<Map<String,String>> lesnotifsInMap = new HashSet<>();
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur.isPresent()){
            System.out.println("JJJJJJJJJJJ"+ utilisateur.get().getNotifications().size());
            for (Notification notification: utilisateur.get().getNotifications()) {
                System.out.println("77777"+ notification.getUtilisateur_qui_envoie());

                Optional<Utilisateur> utilisateurEnvoie = utilisateurRepository.findById(notification.getUtilisateur_qui_envoie());

                if (utilisateurEnvoie.isPresent()) {
                    Map<String, String> lesnotifs = new HashMap<>();
                    lesnotifs.put("notifMessage", notification.getMessage());
                    lesnotifs.put("Date", String.valueOf(notification.getDate()));
                    if (utilisateur.get().getProfil().getPhoto() != null) {
                        lesnotifs.put("photo", utilisateurEnvoie.get().getProfil().getPhoto());
                    } else {
                        lesnotifs.put("photo", null);
                    }
                    lesnotifs.put("pseudo", String.valueOf(utilisateurEnvoie.get().getPseudo()));
                    lesnotifsInMap.add(lesnotifs);
                }
            }
        }
        return lesnotifsInMap;
    }

    /**
     * Retourne un certain format JSON de l'Utilisateur
     * @param user ID de l'Utilisateur
     * @return Infos de l'Utilisateur sous Map<nom, donnée>
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/userInformation/{user}")
    public Map<String, String> userInformation(@PathVariable Long user) throws UnsupportedEncodingException {
        Map<String, String> information = new HashMap<>();
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);

        System.out.println("salut");
        if (utilisateur.isPresent()) {
            Utilisateur userObj = utilisateur.get();

            information.put("idUtilisateur" , String.valueOf(userObj.getId()));
            information.put("countAbonnement", String.valueOf(userObj.countAbonnement()));
            information.put("countAbonne", String.valueOf(userObj.countAbonne()));
            information.put("pseudo", String.valueOf(userObj.getPseudo()));

            Profil profil = userObj.getProfil();
//            if (profil != null) {
            if (profil.getDescription() != null) {
                String encodedDescription = URLEncoder.encode(profil.getDescription(), StandardCharsets.UTF_8);
                information.put("description", encodedDescription);
            }else {
                information.put("description", null);
            }
            if (profil.getPhoto() != null) {
                information.put("photo", profil.getPhoto());
            } else {
                information.put("photo", null);
            }
        }

        return information;
    }

    /**
     * Fonction pour avoir une liste de tout les abonnements d'un utilisateur
     * @param userId ID de l'Utilisateur
     * @return Iterable Utilisateur (abonnements)
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/abonnementUser/{userId}")
    public Set<Map> AbonnementUser(@PathVariable Long userId) throws UnsupportedEncodingException {
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

    /**
     * Fonction pour avoir une liste de tout les abonnés d'un utilisateur
     * @param userId ID de l'Utilisateur
     * @return Iterable Utilisateur (abonnés)
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/abonneUser/{userId}")
    public Set<Map<String,String>> AbonneUser(@PathVariable Long userId) throws UnsupportedEncodingException {
        Set<Map<String,String>> abonneinfo = new HashSet<>();
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(userId);
        if (utilisateurOptional.isPresent()) {

            Utilisateur utilisateur = utilisateurOptional.get();
            for (Utilisateur utilisateur1 : utilisateur.getAbonnes()) {
                abonneinfo.add(userInformation(utilisateur1.getId()));

            }
            System.out.println(abonneinfo);
            return abonneinfo;
        } else {
            // L'utilisateur avec l'ID spécifié n'a pas été trouvé
            return Collections.emptySet();
        }
    }

    /**
     * Fonction pour voir si un abonné est présent dans mes abonnements
     * @param userId ID de l'Utilisateur
     * @param abonneid ID de l'abonné
     * @return Boolean de presence
     */
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

    @GetMapping("/presenceUserNotifie/{userId}/{abonnementid}")
    public boolean presenceUserNotifie(@PathVariable Long userId, @PathVariable Long abonnementid) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(userId);
        Optional<Utilisateur> abonnementUser = utilisateurRepository.findById(abonnementid);
        if (utilisateur.isPresent() && abonnementUser.isPresent()) {
            return abonnementUser.get().verifUserNotifies(utilisateur.get());
        } else {
            return false;
        }
    }

    @GetMapping("/getUtilisateurIdByEmail")
    public Long getUtilisateurIdByEmail(@RequestParam String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email);
        System.out.println(utilisateur.getId());
        return utilisateur.getId();
    }

    /**
     * Retourne l'Utilisateur correspondant à l'ID
     * @param id ID de l'Utilisateur
     * @return Utilisateur
     */
    @GetMapping("/getUtilisateur/{id}")
    public Utilisateur getUtilisateurById(@PathVariable("id") Long id) {
        if (utilisateurRepository.findById(id).isPresent()) {
            Utilisateur user = new Utilisateur();
            user.setId(utilisateurRepository.findById(id).get().getId());
            user.setPseudo(utilisateurRepository.findById(id).get().getPseudo());
            return user;
        }
        return new Utilisateur();
    }

    /**
     * Supprime l'Utilisateur correspondant à l'ID
     * @param id ID de l'Utilisateur
     */
    @Transactional
    @DeleteMapping("/utilisateur/delete/{id}")
    public void deleteUtilisateur( @PathVariable("id") Long id) {
        try {
            avisRepository.deleteAvisByUtilisateurId(id);
            publicationRepository.deletePublicationByProprietaire(id);
            profilRepository.deleteProfilByUtilisateurId(id);
            utilisateurRepository.deleteById(id);
            System.out.println("Utilisateur supprimé avec succès.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur.");
        }
    }

    /**
     * Fonction pour enregistrer les string du compte, pseudo, description
     * @param requestBody Map contenant les infos du Profil
     * @param user ID de l'Utilisateur
     * @return ResponseEntity
     */
    @PostMapping("/updateStringProfil/{user}")
    public ResponseEntity<String> handleFileUpload(
            @RequestBody Map<String, String> requestBody, @PathVariable Long user) {
        System.out.println("salutation77");
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        if (utilisateur.isPresent()){
            utilisateur.get().setPseudo(requestBody.get("pseudo").replaceAll("^\"|\"$", ""));
            utilisateurRepository.save(utilisateur.get());
                Optional<Profil> profil = profilRepository.findById(utilisateur.get().getProfil().getId());
                if (profil.isPresent()){
                    System.out.println("666666666"+requestBody.get("description"));
                    if (requestBody.get("description").length() > 0) {
                        System.out.println(requestBody.get("description").length());
                        profil.get().setDescription(requestBody.get("description").replaceAll("^\"|\"$", ""));
                    }else {
                        profil.get().setDescription(null);
                    }
                    profilRepository.save(profil.get());
                }
        }
        return ResponseEntity.ok("OK");
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

    @GetMapping("/imageProfil/{nomFichier:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String nomFichier) throws MalformedURLException {
        return getResourceResponseEntity(nomFichier, uploadDirImageProfil);
    }

    @PostMapping("/uploadProfil/{user}")
    public ResponseEntity<String> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @PathVariable("user") long user) {
        System.out.println("test1212121221212");
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(user);
        try {
            if (utilisateur.isPresent()) {
//                if (utilisateur.get().getProfil() != null) {

                    if (utilisateur.get().getProfil().getPhoto() != null) {
                        Path filePath = Paths.get(uploadDirImageProfil).resolve(utilisateur.get().getProfil().getPhoto()).normalize();

                        // Vérifier si le fichier existe avant de le supprimer
                        if (Files.exists(filePath)) {
                            Files.delete(filePath);
                            System.out.println("supprime");
                        } else {
                            System.out.println("erreur");
                        }
                    }
                    // Construire le chemin complet pour le nouveau fichier
                    Path targetLocation = Path.of(uploadDirImageProfil).resolve(file.getOriginalFilename());

                    utilisateur.get().getProfil().setPhoto(file.getOriginalFilename());
                    utilisateurRepository.save(utilisateur.get());
                    // Copier le fichier dans le répertoire de destination
                    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                    System.out.println("Fichier enregistré avec succès à : " + targetLocation);

                    return ResponseEntity.ok("Fichier téléchargé avec succès!");
//                } else {
//                    // Construire le chemin complet pour le nouveau fichier
//                    Path targetLocation = Path.of(uploadDirImageProfil).resolve(file.getOriginalFilename());
//
//                    utilisateur.get().getProfil().setPhoto(file.getOriginalFilename());
//                    utilisateurRepository.save(utilisateur.get());
//                    // Copier le fichier dans le répertoire de destination
//                    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//                    System.out.println("Fichier enregistré avec succès à : " + targetLocation);
//                    Profil profil = new Profil(null, file.getOriginalFilename(), null);
//                    utilisateur.get().setProfil(profil);
//                    profilRepository.save(profil);
//                    utilisateurRepository.save(utilisateur.get());
//                    return ResponseEntity.ok("Fichier téléchargé avec succès!");
//                }

            }

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de la suppression du fichier.");
        }
        return null;

    }
}

