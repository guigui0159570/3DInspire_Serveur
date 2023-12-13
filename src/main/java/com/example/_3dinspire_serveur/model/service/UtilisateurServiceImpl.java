package com.example._3dinspire_serveur.model.service;

import com.example._3dinspire_serveur.model.DTO.UtilisateurDTO;
import com.example._3dinspire_serveur.model.Role;
import com.example._3dinspire_serveur.model.Utilisateur;
import com.example._3dinspire_serveur.repository.RoleRepository;
import com.example._3dinspire_serveur.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {
    private UtilisateurRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UtilisateurServiceImpl(UtilisateurRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Utilisateur saveUser(UtilisateurDTO userDto) {
        Utilisateur user = new Utilisateur();
        user.setPseudo(userDto.getPseudo());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByName("ROLE_USER");
        if(role == null){
            role = checkRoleUserExist();
        }
        user.setRoles(Arrays.asList(role));
        return userRepository.save(user);
    }



    public Utilisateur setAdmin(Utilisateur utilisateur){
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleAdminExist();
        }
        utilisateur.getRoles().add(role);
        return userRepository.save(utilisateur);
    }

    @Override
    public Utilisateur findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UtilisateurDTO> findAllUsers() {
        Iterable<Utilisateur> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false)
                .map(user -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public Utilisateur findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }


    private UtilisateurDTO mapToUserDto(Utilisateur user){
        UtilisateurDTO userDto = new UtilisateurDTO();
        userDto.setPseudo(user.getPseudo());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleAdminExist(){
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
    private Role checkRoleUserExist(){
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }
}
