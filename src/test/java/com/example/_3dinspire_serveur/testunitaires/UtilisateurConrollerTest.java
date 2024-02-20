package com.example._3dinspire_serveur.testunitaires;

import com.example._3dinspire_serveur.model.Profil;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.ProfilRepository;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UtilisateurConrollerTest {

    private final MockMvc mockMvc;

    private Utilisateur utilisateur;

    private final ProfilRepository profilRepository;

    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public UtilisateurConrollerTest(UtilisateurRepository utilisateurRepository,
                                ProfilRepository profilRepository,
                                MockMvc mockMvc){
        this.utilisateurRepository = utilisateurRepository;
        this.profilRepository = profilRepository;
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    void inittialise(){
        Profil profil = new Profil();
        profil.setUtilisateur(utilisateur);

        profilRepository.save(profil);
        utilisateur = new Utilisateur("admin.admin@admin.com", "abcd", "abcd");
        utilisateur.setProfil(profil);

        utilisateurRepository.save(utilisateur);
    }

    @AfterAll
    void clean(){
        utilisateurRepository.deleteAll();
        profilRepository.deleteAll();

        utilisateur = null;
    }

    @Test
    @DisplayName("Test récupérer un user")
    void testGetUser() throws Exception {
        var request = MockMvcRequestBuilders.get("/user");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
