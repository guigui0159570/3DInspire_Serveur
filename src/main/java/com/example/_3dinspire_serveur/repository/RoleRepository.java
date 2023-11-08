package com.example._3dinspire_serveur.repository;

import com.example._3dinspire_serveur.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
