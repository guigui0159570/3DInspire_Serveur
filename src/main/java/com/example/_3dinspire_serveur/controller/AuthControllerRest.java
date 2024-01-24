package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.DTO.UtilisateurDTO;
import com.example._3dinspire_serveur.model.Profil;
import com.example._3dinspire_serveur.repository.ProfilRepository;
import com.example._3dinspire_serveur.repository.UserRespository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.model.request.LoginReq;
import com.example._3dinspire_serveur.model.response.ErrorRes;
import com.example._3dinspire_serveur.model.response.LoginRes;
import com.example._3dinspire_serveur.model.security.JwtUtil;
import com.example._3dinspire_serveur.model.service.UtilisateurService;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller qui sert à la connexion, l'inscription
 */
@RestController
@RequestMapping("/api/auth")
public class AuthControllerRest {
    private Environment env;
    private UtilisateurService userService;
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserRespository userRespository;
    private UtilisateurRepository utilisateurRepository;
    private ProfilRepository profilRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public void MailService(Environment env){
        this.env = env;
    }

    /**
     * Constructeur auto-générer
     * @param utilisateurService Model
     * @param authenticationManager Model
     * @param jwtUtil Variable de la librairie JWT Token
     * @param utilisateurRepository Model
     * @param UserRepository Model
     * @param profilRepository Model
     */
    public AuthControllerRest(UtilisateurService utilisateurService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UtilisateurRepository utilisateurRepository, UserRespository UserRepository, ProfilRepository profilRepository) {
        this.userService = utilisateurService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.utilisateurRepository = utilisateurRepository;
        this.userRespository = UserRepository;
        this.profilRepository = profilRepository;
    }

    /**
     * Mapping d'inscription (accès ouvert)
     * @param userDto Informations du nouveau Utilisateur (pseudo, mail, mot de passe)
     * @param result Vérificateur d'erreur du DTO
     * @param model Model
     * @return HTTP Status Ok ou BadRequest avec l'erreur
     */
    @Transactional
    @PostMapping("/register/save")
    public ResponseEntity<Utilisateur> registration(@Valid @ModelAttribute("user") UtilisateurDTO userDto,
                                       BindingResult result,
                                       Model model) {
        Utilisateur existingUser = userService.findUserByEmail(userDto.getEmail());
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "Un compte existe déjà sous cette adresse email.");
        }

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            Utilisateur u = userService.saveUser(userDto);
            Profil profil = new Profil(null, null);
            profil = profilRepository.save(profil);
            u.setProfil(profil);
            System.out.println(u);
            return ResponseEntity.ok(u);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    /**
     * Vérifie si le compte est valide
     * @param userDto Informations du nouveau Utilisateur (pseudo, mail, mot de passe)
     * @param result Vérificateur d'erreur du DTO
     * @param model Model
     * @return HTTP Status Ok si existe ou BadRequest avec l'erreur
     */
    @GetMapping("/login/verif")
    public ResponseEntity<Utilisateur> login(@Valid @ModelAttribute("user") UtilisateurDTO userDto,
                                             BindingResult result,
                                             Model model){
        Utilisateur existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser == null){
            result.rejectValue("email", null,
                    "There is no account registered with the same email");
        }

        if(result.hasErrors()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Utilisateur u = userService.findUserByEmail(userDto.getEmail());
        return ResponseEntity.ok(u);
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/validate-token")
    public ResponseEntity<ValidationResponse> checkSession() {
        System.out.println("Session valide");
        ValidationResponse response = new ValidationResponse("Session valide");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Classe représentant la réponse JSON
     */
    private static class ValidationResponse {
        private final String message;

        public ValidationResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * Se connecter via email et mot de passe
     * @param loginReq
     * @return ResponseEntity : Ok si connecter, BadRequest si non reconnu
     */
    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginReq loginReq)  {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            String email = authentication.getName();
            UtilisateurDTO user = new UtilisateurDTO(email, "", "");
            String token = jwtUtil.createToken(user);
            LoginRes loginRes = new LoginRes(email,token);

            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Renvoie l'email de l'Utilisateur grâce au Token JWT
     * @return ResponseEntity Ok avec le mail
     */
    @GetMapping("/get-user-email")
    public ResponseEntity<String> getUserEmail() {
        // Obtenez l'e-mail de l'utilisateur à partir du contexte de sécurité
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userEmail);
    }

    /**
     * Envoie le mail à l'Utilisateur si il existe
     * @param email Chaine de caractères de l'email
     * @return ResponseEntity : Ok si connecter, BadRequest si non reconnu
     */
    @PostMapping("/resetPassword")
    public ResponseEntity<Void> resetPassword(@RequestParam String email) {
        // Vérifiez si l'e-mail existe dans votre système
        Utilisateur user = userService.findUserByEmail(email);

        if (user == null) {
            System.out.println("l'utilisateur n'existe pas");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Générez un jeton unique pour la réinitialisation du mot de passe
        String resetToken = generateResetToken();

        System.out.println("Reset Token: " + resetToken);
        userRespository.UpdateTokenUtilisateur(resetToken,email);

        sendPasswordResetEmail(user.getEmail(),resetToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Génère un Token de reinitialisation
     * @return UUID randomiser
     */
    private String generateResetToken() {
        // Générez un jeton unique ici (par exemple, UUID.randomUUID().toString())
        return UUID.randomUUID().toString();
    }

    /**
     * Envoie le mail
     * @param to Email destinataire
     * @param resetToken Token générer aléatoirement
     */
    private void sendPasswordResetEmail(String to, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("3dinspire10@gmail.com");
            message.setTo(to);
            message.setSubject("Réinitialisation de mot de passe");
            String resetLink = "http://10.6.2.252:8080/api/auth/mail-password?token="+resetToken;
            message.setText("Cliquez sur le lien suivant pour réinitialiser votre mot de passe: \n" + resetLink);

            System.out.println("About to send email...");

            javaMailSender.send(message);

            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            System.out.println("Failed to send email. Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
