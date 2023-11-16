package com.example._3dinspire_serveur.model.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Imprimez les détails de la requête ici
        System.out.println("Requête reçue: " + request.getMethod() + " " + request.getRequestURI());
        // Vous pouvez également imprimer les en-têtes, les paramètres, etc. selon vos besoins

        // Continuez la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
