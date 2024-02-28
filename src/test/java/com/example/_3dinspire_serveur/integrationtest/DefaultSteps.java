package com.example._3dinspire_serveur.integrationtest;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class DefaultSteps {
    private final EntityManager entityManager;

    @Autowired
    public DefaultSteps(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Nettoie la base de données avant chaque test.
     */
    @Before @After
    public void clean() {
        entityManager.clear();
    }

}
