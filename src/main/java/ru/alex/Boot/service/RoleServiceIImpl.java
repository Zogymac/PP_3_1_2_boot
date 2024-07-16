package ru.alex.Boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alex.Boot.model.Role;
import ru.alex.Boot.repository.RoleRepository;

import java.util.List;

@Service
public class RoleServiceIImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceIImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
