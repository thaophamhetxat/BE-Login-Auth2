package com.example.beloginauth2.Service;

import com.example.beloginauth2.Model.Role;
import com.example.beloginauth2.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles() {
        return (List<Role>) roleRepository.findAll();
    }
}
