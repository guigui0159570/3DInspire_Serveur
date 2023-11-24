package com.example._3dinspire_serveur.controller;

import com.example._3dinspire_serveur.model.DTO.UtilisateurDTO;
import com.example._3dinspire_serveur.model.LoginRequest;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.model.request.LoginReq;
import com.example._3dinspire_serveur.model.response.ErrorRes;
import com.example._3dinspire_serveur.model.response.LoginRes;
import com.example._3dinspire_serveur.model.security.JwtUtil;
import com.example._3dinspire_serveur.model.service.UtilisateurService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.management.remote.JMXAuthenticator;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private UtilisateurService userService;
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    public AuthController(UtilisateurService utilisateurService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = utilisateurService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/register/save")
    public ResponseEntity<Utilisateur> registration(@Valid @ModelAttribute("user") UtilisateurDTO userDto,
                                       BindingResult result,
                                       Model model){
        Utilisateur existingUser = userService.findUserByEmail(userDto.getEmail());
        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "Un compte existe déjà sous cette adresse email.");
        }

        if(result.hasErrors()){
            // model.addAttribute("user", userDto);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Utilisateur u = userService.saveUser(userDto);
        return ResponseEntity.ok(u);
    }

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

    @ResponseBody
    @GetMapping("/validate-token")
    public ResponseEntity<ValidationResponse> checkSession() {
        System.out.println("Session valide");
        ValidationResponse response = new ValidationResponse("Session valide");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Classe représentant la réponse JSON
    private static class ValidationResponse {
        private final String message;

        public ValidationResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

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
}
