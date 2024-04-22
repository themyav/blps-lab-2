package com.blps.lab3.service;

import com.blps.lab3.model.Privilege;
import com.blps.lab3.model.Role;
import com.blps.lab3.repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public void create(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }

    public Role findByName(String name){
        if(name == null) return null; //TODO check
        return roleRepository.findByName(name);
    }
}
