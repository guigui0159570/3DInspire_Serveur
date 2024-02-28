package com.example._3dinspire_serveur.integrationtest;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class CreateAccountSteps {
    private final MockMvc mockMvc;
    private MockHttpServletRequestBuilder request;
    private ResultActions response;

    @Autowired
    public CreateAccountSteps(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Given("L'utilisateur se connecte sur la page d'inscription")
    public void createRequest() {
        request = MockMvcRequestBuilders.post("/api/auth/register/save");
    }

    @Given("Un utilisateur {string} est inscrit avec l'email {string}")
    public void registerUser(String pseudo, String email) throws Exception {
        var request = MockMvcRequestBuilders.post("/api/auth/register/save")
                .param("pseudo", pseudo)
                .param("email", email)
                .param("password", "Password@123");

        mockMvc.perform(request);
    }

    @And("L'utilisateur entre son email {string}, son pseudo {string} et un mot de passe {string}")
    public void enterInformations(String email, String pseudo, String password) {
        request = request.param("email", email)
                .param("pseudo", pseudo)
                .param("password", password);
    }

    @When("L'utilisateur envoie ses informations")
    public void submit() throws Exception {
        response = mockMvc.perform(request);
    }

    @Then("L'utilisateur est inscrit")
    public void isOk() throws Exception {
        response.andExpect(status().isOk());
    }

    @Then("L'utilisateur n'est pas inscrit")
    public void isBadRequest() throws Exception {
        response.andExpect(status().isBadRequest());
    }

}
